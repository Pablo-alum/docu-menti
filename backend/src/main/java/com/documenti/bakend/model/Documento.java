package com.documenti.bakend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.documenti.bakend.enums.EstadoDocumento;
//import com.documenti.bakend.enums.EstadoProcesamientoIA;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documentos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // Hibernate lo usa, nadie más
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "documento_id")
    private UUID id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "cofre", nullable = false)
    private String cofre;

    @Column(name = "storage_key", length = 1024, nullable = false)
    private String storageKey;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "tamaño")
    private Long tamaño;

    @Column(name = "hash_sha256")
    private String hash;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoDocumento estado;

    //@Enumerated(EnumType.STRING)
    //@Column(name = "estado_ia")
    //private EstadoProcesamientoIA estadoIA;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @ManyToOne
    @JoinColumn(name = "asunto_id")
    private Asunto asunto;

    @ManyToOne
    @JoinColumn(name = "creado_por", nullable = false)
    private Usuario creadoPor;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
    @ManyToMany
    @JoinTable(name = "Documento_Categorias",
         joinColumns  = @JoinColumn(name ="categorias_id"),
         inverseJoinColumns =  @JoinColumn(name ="documento_id")
    )
    private List<Categorias> categorias = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "Documento_Etiquetas",
    joinColumns = @JoinColumn(name ="etiquetas_id"),
    inverseJoinColumns = @JoinColumn(name = "documento_id")
    )
    private List<Etiquetas> etiquetas = new ArrayList<>();
    // ========== MÉTODOS DE FÁBRICA (creación controlada) ==========

    public static Documento crear(String nombre, String cofre, String storageKey,
                                  String mimeType, Long tamaño, String hash,
                                  Map<String, Object> metadata, Asunto asunto, 
                                  Usuario creadoPor , List<Etiquetas> etiquetas) {
        Documento document = new Documento();
        document.nombre = nombre;
        document.cofre = cofre;
        document.storageKey = storageKey;
        document.mimeType = mimeType;
        document.tamaño = tamaño;
        document.hash = hash;
        document.metadata = metadata != null ? metadata : new HashMap<>();
        document.asunto = asunto;
        document.creadoPor = creadoPor;
        document.estado = EstadoDocumento.BORRADOR;
        document.etiquetas = etiquetas != null ? etiquetas : new ArrayList<>();
        return document;
    }

    // ========== MÉTODOS DE DOMINIO (cambio de estado controlado) ==========

    public void enviarARevision() {
        if (this.estado != EstadoDocumento.BORRADOR) {
            throw new IllegalStateException("Solo se puede revisar desde BORRADOR");
        }
        this.estado = EstadoDocumento.REVISION;
    }

    public void aprobar() {
        if (this.estado != EstadoDocumento.REVISION) {
            throw new IllegalStateException("Solo se puede aprobar desde REVISION");
        }
        this.estado = EstadoDocumento.APROBADO;
    }

    public void actualizarMetadata(String clave, Object valor) {
        this.metadata.put(clave, valor);
    }

    public String getNombre() {
        return nombre;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getTamaño() {
        return tamaño;
    }

    

    public EstadoDocumento getEstado() {
        return estado;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public Asunto getAsunto() {
        return asunto;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

}
