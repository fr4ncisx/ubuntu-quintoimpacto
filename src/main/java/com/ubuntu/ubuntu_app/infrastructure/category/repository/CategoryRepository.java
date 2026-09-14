package com.ubuntu.ubuntu_app.infrastructure.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{
    Optional<CategoryEntity> findByName(String name);
}
