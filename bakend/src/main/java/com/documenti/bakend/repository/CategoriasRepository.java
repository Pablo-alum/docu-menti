package com.documenti.bakend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.documenti.bakend.model.Categorias;

/**
 * CategoriasRepository
 */
@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, UUID>{
    Optional<Categorias> findByNombre(String nombre);
    
}
