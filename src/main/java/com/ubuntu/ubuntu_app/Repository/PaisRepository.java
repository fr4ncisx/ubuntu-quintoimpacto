package com.ubuntu.ubuntu_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.PaisEntity;

import java.util.Optional;

@Repository
public interface PaisRepository extends JpaRepository<PaisEntity,Long> {

    Optional<PaisEntity> findByNombre(String nombrePais);
}
