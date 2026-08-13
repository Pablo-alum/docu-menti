package com.documenti.bakend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.documenti.bakend.model.Documento;

/**
 * DocumentoRepository
 */
@Repository
public interface DocumentoRepository  extends JpaRepository<Documento, UUID>{
    Optional<Documento> findByNombre(String nombre);
    
}
