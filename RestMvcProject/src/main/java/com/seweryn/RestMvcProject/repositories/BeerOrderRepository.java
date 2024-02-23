package com.seweryn.RestMvcProject.repositories;

import com.seweryn.RestMvcProject.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {

}
