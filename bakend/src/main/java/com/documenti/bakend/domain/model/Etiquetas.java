package com.documenti.bakend.domain.model;


import java.util.ArrayList;
import java.util.List;

import com.documenti.bakend.domain.model.base.BaseIdNombre;

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
    private List<Documento> ListaDocumentos = new ArrayList<>();
}
