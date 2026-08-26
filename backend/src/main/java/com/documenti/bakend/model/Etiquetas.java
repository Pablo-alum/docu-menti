package com.documenti.bakend.model;


import java.util.ArrayList;
import java.util.List;

import com.documenti.bakend.model.base.BaseIdNombre;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Etiquetas
 */
@Getter
@Entity
@Setter
@Table(name = "etiqueta")

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Etiquetas extends BaseIdNombre {
    @ManyToMany
    private List<Documento> Documentos = new ArrayList<>();
    private String hex_color;

    public Etiquetas(String nombre, List<Documento> documento, String hex_color) {
        super(nombre);
        Documentos = documento != null ? documento : new ArrayList<>();
        this.hex_color = hex_color;
    }
    public void setDocumentos(List<Documento> listaDocumentos) {
        Documentos = listaDocumentos;
    }
    public void setHex_color(String hex_color) {
        this.hex_color = hex_color;
    }
    


}
