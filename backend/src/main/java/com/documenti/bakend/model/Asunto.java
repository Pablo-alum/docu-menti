package com.documenti.bakend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Asunto
 */

@Table(name = "asuntos")
@Getter
@NoArgsConstructor(access =  lombok.AccessLevel.PROTECTED)
@Entity
public class Asunto {

    
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id   
    private UUID id;

    @Column(name = "caratula", nullable = false)
    private String caratula;

    // METADATOS DEL DOMINIO: acá va lo que cambia según sea legal, contable, médico
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata = new HashMap<>();

    @OneToMany(mappedBy = "asunto")
    private List<Documento> documentos = new ArrayList<>();

    public Asunto(String caratula, Map<String, Object> metadata, List<Documento> documentos) {
        if (caratula == null    || caratula.isBlank()) {
            throw new IllegalArgumentException("la carcula es obligatoria");
        }
        this.caratula = caratula ;
        this.metadata = metadata != null ? metadata : new HashMap<>(); 
        this.documentos = documentos != null ? documentos : new ArrayList<>();
    }
    public void AgregarDocumento(Documento documento) { documentos.add(documento); }
    public List<Documento> getDocumentos(){
     return List.copyOf(documentos);
    }
    public void ActualisarMetadta(String clave, String valor){
        this.metadata.put(clave, valor);
    }

    public String getCaratula() {
        return caratula;
    }
    public Map<String, Object> getMetadata() {
        return metadata;
    }


}
