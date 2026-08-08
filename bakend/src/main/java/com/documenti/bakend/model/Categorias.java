package com.documenti.bakend.model;


import java.util.ArrayList;

import com.documenti.bakend.model.base.BaseIdNombre;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

  //Categorias
 
@Getter
@Setter
@Table(name = "Categoria")
@NoArgsConstructor
@Entity
public class Categorias extends BaseIdNombre{
    //private ArrayList<Documento> documentos = new ArrayList<>();
    //private ArrayList<Usuario> usuarios = new ArrayList<>();
    
}
