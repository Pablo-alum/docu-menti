package com.documenti.bakend.domain.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Documento
 */
@Entity
@Table(name = "Documento")
@NoArgsConstructor
@Builder
public class Documento {
    
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "DocumentoId")
    private UUID id;

    @Getter
    @Setter
    @Column(name = "DocumentiNombre")
    private String nombreStrign;

    @CreationTimestamp
    @Getter
    private LocalDateTime nombreCreacion;
    private List<String> hisotiral_Camibos = new ArrayList<>();

    @NotNull
    @NotBlank(message = "No tamaño")
    private long tamaño;
    @NotBlank(message = "Error sin dueño")
    private Usuario dueño = new Usuario();
    enum Estado{
        PENDIENTE,
        CLASIFICADO,
        CLASIFICADO_IA,
        ERROR_CLASIFICADO
    }
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private Estado estado;
    private List<categorias> categorias = new ArrayList<>();

    @Column(name = "cofre", nullable = false)
    @Getter @Setter
    private String cofre;

    @Getter @Setter
    @Column(name = "key", length = 1024 , nullable = false)
    private String key;
}
