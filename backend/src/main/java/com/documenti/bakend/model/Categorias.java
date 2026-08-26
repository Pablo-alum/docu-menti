package com.documenti.bakend.model;


import java.util.ArrayList;
import java.util.HashSet;

import com.documenti.bakend.model.base.BaseIdNombre;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

  //Categorias
 
@Getter
@Setter
@Table(name = "Categoria")
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Categorias extends BaseIdNombre{
    @ManyToMany
    private ArrayList<Documento> documentos = new ArrayList<>();
    private String hex_color;
    private String Icono;

    public Categorias(String nombre,ArrayList<Documento> documentos, String hex_color, String icono) {
        super(nombre);
        this.documentos = documentos != null ? documentos : new ArrayList<>();
        this.hex_color = hex_color;
        this.Icono = icono;
    }

    public void setDocumentos(ArrayList<Documento> documentos) {
        this.documentos = documentos;
    }

    public void setHex_color(String hex_color) {
        this.hex_color = hex_color;
    }

    public void setIcono(String icono) {
        Icono = icono;
    }
    
    
}
