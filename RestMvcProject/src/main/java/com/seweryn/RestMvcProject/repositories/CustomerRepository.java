package com.seweryn.RestMvcProject.repositories;

import com.seweryn.RestMvcProject.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
