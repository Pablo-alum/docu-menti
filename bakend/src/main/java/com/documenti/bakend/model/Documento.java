package com.documenti.bakend.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.documenti.bakend.enums.EstadoDocumento;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Documento
 */
@Entity
@Table(name = "Documento")
@NoArgsConstructor
public class Documento {
    
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "DocumentoId")
    private UUID id;

    @Getter
    @Setter
    @Column(name = "DocumentiNombre")
    private String nombre;

    @CreationTimestamp
    @Getter
    private LocalDateTime nombreCreacion;
    //@ElementCollection
    //@CollectionTable(name = "Historial")
    //private List<String> hisotiral_Camibos = new ArrayList<>();

    @NotNull
    @NotBlank(message = "No tamaño")
    private long tamaño;
/*    @ElementCollection*/
    /*@CollectionTable(name = "documento_usuario")*/
    /*@JoinColumn(name = "Usario_id")*/
    /*@NotBlank(message = "Error sin dueño")*/
    /*private Usuario dueño;*/
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private EstadoDocumento estado;
    //private List<Categorias> categorias = new ArrayList<>();

    @Column(name = "cofre", nullable = false)
    @Getter @Setter
    private String cofre;

    @Getter @Setter
    @Column(name = "key", length = 1024 , nullable = false)
    private String key;
}
