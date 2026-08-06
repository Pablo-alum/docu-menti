package com.documenti.bakend.domain.model.base;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
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
@MappedSuperclass public class BaseIdNombre{
    @Id @GeneratedValue(strategy =GenerationType.UUID)
    private UUID id;
    @Size(max = 120,message = "no mas de 120 caracteres")
    @NotBlank(message = "por favor poner un nombre")
    @Column(nullable = "Nombre")
    private String  Nombre;
}
