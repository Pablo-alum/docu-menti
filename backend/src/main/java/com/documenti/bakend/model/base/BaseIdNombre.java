package com.documenti.bakend.model.base;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BaseIdNombre
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass 
public class BaseIdNombre{
    @Id @GeneratedValue(strategy =GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String  nombre;
    protected BaseIdNombre(String nombre) {
    if (nombre == null || nombre.isBlank()) {
        throw new IllegalArgumentException("El nombre no puede estar vacío");
    }
        this.nombre = nombre;
    }

}
