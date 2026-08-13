package com.documenti.bakend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.documenti.bakend.model.Etiquetas;

/**
 * EtiquetasRepository
 */
@Repository
public interface EtiquetasRepository extends JpaRepository<Etiquetas,UUID>{
    Optional<Etiquetas> findByNombre(String nombre);
}
