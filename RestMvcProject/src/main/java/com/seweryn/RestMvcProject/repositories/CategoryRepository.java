package com.seweryn.RestMvcProject.repositories;

import com.seweryn.RestMvcProject.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
