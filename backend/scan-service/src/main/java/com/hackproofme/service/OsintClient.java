package com.hackproofme.service;

import com.hackproofme.model.dto.BreachResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OsintClient {

    @Value("${osint.service.url:http://localhost:8082}")
    private String osintServiceUrl;

    private final RestTemplate restTemplate;

    public OsintClient() {
        this.restTemplate = new RestTemplate();
    }

    public BreachResponse checkBreaches(String identifierHash) {
        try {
            String url = osintServiceUrl + "/api/v1/breach/check/" + identifierHash;
            log.info("Calling OSINT service: {}", url);

            return restTemplate.getForObject(url, BreachResponse.class);
        } catch (Exception e) {
            log.error("Error calling OSINT service", e);
            return BreachResponse.builder()
                    .identifier(identifierHash)
                    .breachCount(0)
                    .breaches(java.util.Collections.emptyList())
                    .build();
        }
    }
}
