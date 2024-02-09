package com.seweryn.RestMvcProject.repositories;

import com.seweryn.RestMvcProject.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
