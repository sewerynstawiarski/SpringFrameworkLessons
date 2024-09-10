package com.seweryn.RestMvcProject.repositories;

import com.seweryn.RestMvcProject.entities.BeerAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerAuditRepository extends JpaRepository<BeerAudit, UUID> {
}
