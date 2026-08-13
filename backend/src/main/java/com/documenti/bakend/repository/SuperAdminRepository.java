package com.documenti.bakend.repository;

import java.util.Optional;
import java.util.UUID;

import com.documenti.bakend.model.Super_admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SuperAdminRepository
 */
@Repository
public interface SuperAdminRepository extends JpaRepository<Super_admin, UUID> {

    Optional<SuperAdminRepository> findByNombre(String nombre);
    
}
