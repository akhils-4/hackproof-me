package com.hackproofme.model;

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
    private List<BreachData> breaches;
}