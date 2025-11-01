package com.hackproofme.service;

import com.hackproofme.exception.ScanNotFoundException;
import com.hackproofme.model.dto.BreachResponse;
import com.hackproofme.model.dto.ScanRequest;
import com.hackproofme.model.dto.ScanResultDTO;
import com.hackproofme.model.entity.ScanResult;
import com.hackproofme.model.enums.RiskLevel;
import com.hackproofme.repository.ScanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScanService {

    private final ScanRepository scanRepository;
    private final PasswordStrengthAnalyzer passwordAnalyzer;
    private final OsintClient osintClient;

    @Transactional
    public ScanResultDTO executeScan(ScanRequest request) {
        log.info("Executing scan for identifier: {}", maskIdentifier(request.getIdentifier()));

        String identifierHash = hashIdentifier(request.getIdentifier());

        BreachResponse breachResponse = osintClient.checkBreaches(identifierHash);
        int breachCount = breachResponse.getBreachCount();

        PasswordStrengthAnalyzer.PasswordAnalysisResult passwordAnalysis =
                passwordAnalyzer.analyze(request.getPassword());
        int passwordScore = passwordAnalysis.getScore();

        int exposureScore = calculateExposureScore(request.getIdentifier());

        int finalScore = calculateFinalScore(breachCount, passwordScore, exposureScore);

        RiskLevel riskLevel = determineRiskLevel(finalScore);

        List<String> suggestions = generateSuggestions(
                breachCount, passwordScore, exposureScore, passwordAnalysis.getSuggestions()
        );

        ScanResult scanResult = ScanResult.builder()
                .identifier(maskIdentifier(request.getIdentifier()))
                .identifierHash(identifierHash)
                .breachCount(breachCount)
                .passwordScore(passwordScore)
                .exposureScore(exposureScore)
                .finalScore(finalScore)
                .riskLevel(riskLevel)
                .suggestions(suggestions)
                .build();

        ScanResult saved = scanRepository.save(scanResult);
        log.info("Scan completed. ID: {}, Score: {}, Risk: {}",
                saved.getId(), finalScore, riskLevel);

        return mapToDTO(saved);
    }

    public ScanResultDTO getScanById(UUID scanId) {
        ScanResult scanResult = scanRepository.findById(scanId)
                .orElseThrow(() -> new ScanNotFoundException("Scan not found with ID: " + scanId));

        return mapToDTO(scanResult);
    }

    public Page<ScanResultDTO> getScanHistory(Pageable pageable) {
        return scanRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapToDTO);
    }

    private int calculateFinalScore(int breachCount, int passwordScore, int exposureScore) {
        int breachImpact = breachCount > 0 ? Math.max(0, 100 - (breachCount * 20)) : 100;
        int passwordRisk = passwordScore;
        int exposureWeight = exposureScore;

        double finalScore = (breachImpact * 0.4) + (passwordRisk * 0.35) + (exposureWeight * 0.25);

        return (int) Math.round(finalScore);
    }

    private RiskLevel determineRiskLevel(int finalScore) {
        if (finalScore >= 80) return RiskLevel.LOW;
        if (finalScore >= 60) return RiskLevel.MEDIUM;
        if (finalScore >= 40) return RiskLevel.HIGH;
        return RiskLevel.CRITICAL;
    }

    private int calculateExposureScore(String identifier) {
        int score = 50;

        if (identifier.contains("@")) {
            score -= 10;
        }

        if (identifier.matches(".*\\d{4,}.*")) {
            score -= 15;
        }

        if (identifier.length() > 15) {
            score += 10;
        }

        return Math.max(0, Math.min(100, score));
    }

    private List<String> generateSuggestions(int breachCount, int passwordScore,
                                             int exposureScore, List<String> passwordSuggestions) {
        List<String> suggestions = new ArrayList<>();

        if (breachCount > 0) {
            suggestions.add("Your identifier was found in " + breachCount +
                    " data breach(es). Change passwords immediately on affected accounts.");
            suggestions.add("Enable two-factor authentication (2FA) on all accounts");
        }

        if (passwordScore < 60) {
            suggestions.addAll(passwordSuggestions);
            suggestions.add("Use a password manager to generate and store strong passwords");
        }

        if (exposureScore < 50) {
            suggestions.add("Consider using unique identifiers for different services");
            suggestions.add("Review and limit information shared on social media");
        }

        if (suggestions.isEmpty()) {
            suggestions.add("Your security posture is good! Keep monitoring regularly.");
            suggestions.add("Stay updated on the latest security best practices");
        }

        return suggestions;
    }

    private String hashIdentifier(String identifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(identifier.toLowerCase().getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not found", e);
            return identifier;
        }
    }

    private String maskIdentifier(String identifier) {
        if (identifier == null || identifier.length() <= 3) {
            return "***";
        }

        if (identifier.contains("@")) {
            String[] parts = identifier.split("@");
            String masked = parts[0].charAt(0) + "***";
            return masked + "@" + parts[1];
        }

        return identifier.charAt(0) + "***" + identifier.charAt(identifier.length() - 1);
    }

    private ScanResultDTO mapToDTO(ScanResult entity) {
        return ScanResultDTO.builder()
                .scanId(entity.getId())
                .identifier(entity.getIdentifier())
                .breachCount(entity.getBreachCount())
                .passwordScore(entity.getPasswordScore())
                .exposureScore(entity.getExposureScore())
                .finalScore(entity.getFinalScore())
                .riskLevel(entity.getRiskLevel())
                .suggestions(entity.getSuggestions())
                .timestamp(entity.getCreatedAt())
                .build();
    }
}