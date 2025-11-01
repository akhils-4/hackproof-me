package com.hackproofme.repository;


import com.hackproofme.model.entity.ScanResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScanRepository extends JpaRepository<ScanResult, UUID> {

    Optional<ScanResult> findByIdentifierHash(String identifierHash);

    Page<ScanResult> findAllByOrderByCreatedAtDesc(Pageable pageable);
}