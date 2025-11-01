package com.hackproofme.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreachResponse {
    private String identifier;
    private Integer breachCount;
    private List<BreachInfo> breaches;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreachInfo {
        private String name;
        private String date;
        private List<String> dataClasses;
    }
}