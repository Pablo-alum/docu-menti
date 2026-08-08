package com.documenti.bakend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.documenti.bakend.model.Contacto;

/**
 * ContactoRepository
 */
@Repository
public interface ContactoRepository extends JpaRepository<Contacto, UUID>{

    Optional<Contacto> findByNombre(String nombre);

}
