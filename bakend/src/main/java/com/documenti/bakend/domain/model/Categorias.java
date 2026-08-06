package com.documenti.bakend.domain.model;


import java.util.ArrayList;
import java.util.List;

import com.documenti.bakend.domain.model.base.BaseIdNombre;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * categorias
 */
@Getter
@Setter
@Table(name = "Categoria")
@NoArgsConstructor
@Entity
public class categorias extends BaseIdNombre{
    private List<Documento> ListaDocumentos = new ArrayList<>();
    
}
