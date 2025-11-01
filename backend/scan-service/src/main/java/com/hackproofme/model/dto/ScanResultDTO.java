package com.hackproofme.model.dto;


import com.hackproofme.model.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanResultDTO {
    private UUID scanId;
    private String identifier;
    private Integer breachCount;
    private Integer passwordScore;
    private Integer exposureScore;
    private Integer finalScore;
    private RiskLevel riskLevel;
    private List<String> suggestions;
    private LocalDateTime timestamp;
}