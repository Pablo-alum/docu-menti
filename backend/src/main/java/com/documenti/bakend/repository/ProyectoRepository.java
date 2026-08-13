package com.documenti.bakend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.documenti.bakend.model.Proyecto;

/**
 * ProyectoRepository
 */
@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto,UUID>{
    Optional<Proyecto> findByNombre(String nombre);
    
}
