package com.ubuntu.ubuntu_app.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{
    Optional<CategoryEntity> findByNombre(String nombre);
}
