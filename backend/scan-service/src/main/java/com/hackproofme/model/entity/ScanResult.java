package com.hackproofme.model.entity;

import com.hackproofme.model.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "scan_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ScanResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String identifier;

    @Column(name = "identifier_hash", unique = true, length = 64)
    private String identifierHash;

    @Column(name = "breach_count")
    private Integer breachCount;

    @Column(name = "password_score")
    private Integer passwordScore;

    @Column(name = "exposure_score")
    private Integer exposureScore;

    @Column(name = "final_score")
    private Integer finalScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", length = 20)
    private RiskLevel riskLevel;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "scan_suggestions",
            joinColumns = @JoinColumn(name = "scan_id")
    )
    @Column(name = "suggestion", length = 1000, columnDefinition = "TEXT")
    private List<String> suggestions = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
