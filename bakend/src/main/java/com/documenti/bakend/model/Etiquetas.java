package com.documenti.bakend.model;


import com.documenti.bakend.model.base.BaseIdNombre;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Etiquetas
 */
@Getter
@Entity
@Setter
@Table(name = "etiqueta")
public class Etiquetas extends BaseIdNombre {
    //private List<Documento> ListaDocumentos = new ArrayList<>();
    private String color;
}
