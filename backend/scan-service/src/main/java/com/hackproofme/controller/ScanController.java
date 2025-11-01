package com.hackproofme.controller;


import com.hackproofme.model.dto.ScanRequest;
import com.hackproofme.model.dto.ScanResultDTO;
import com.hackproofme.service.ScanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scan")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Scan API", description = "Endpoints for security scanning")
@CrossOrigin(origins = "*")
public class ScanController {

    private final ScanService scanService;

    @PostMapping
    @Operation(summary = "Execute security scan")
    public ResponseEntity<ScanResultDTO> executeScan(@Valid @RequestBody ScanRequest request) {
        log.info("Received scan request");
        ScanResultDTO result = scanService.executeScan(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{scanId}")
    @Operation(summary = "Get scan by ID")
    public ResponseEntity<ScanResultDTO> getScanById(@PathVariable UUID scanId) {
        log.info("Fetching scan with ID: {}", scanId);
        ScanResultDTO result = scanService.getScanById(scanId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    @Operation(summary = "Get scan history")
    public ResponseEntity<Page<ScanResultDTO>> getScanHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Fetching scan history - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ScanResultDTO> results = scanService.getScanHistory(pageable);

        return ResponseEntity.ok(results);
    }
}
