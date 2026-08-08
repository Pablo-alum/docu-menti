package com.documenti.bakend.model.base;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * Padre
 */
@MappedSuperclass
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED) 
public class Padre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    private UUID idUUID;

    @NotBlank(message = "Por favor poner nombre")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "Por favor poner contraseña")
    @Size(min = 8, message = "Mínimo 8 caracteres")
    @Column(name = "correo", nullable = false)
    private String contraseña;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime creadoEn;
    
}
