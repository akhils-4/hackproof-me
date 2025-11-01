package com.hackproofme.controller;



import com.hackproofme.model.BreachResponse;
import com.hackproofme.service.BreachLookupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/breach")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BreachController {

    private final BreachLookupService breachLookupService;

    @GetMapping("/check/{identifierHash}")
    public ResponseEntity<BreachResponse> checkBreaches(@PathVariable String identifierHash) {
        log.info("Received breach check request");
        BreachResponse response = breachLookupService.checkBreaches(identifierHash);
        return ResponseEntity.ok(response);
    }
}