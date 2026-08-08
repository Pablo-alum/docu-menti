package com.documenti.bakend.model;


import com.documenti.bakend.model.base.BaseIdNombre;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Proyecto
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Proyecto extends BaseIdNombre  {

    private String color;
    //@NotBlank(message =  "No hay dueño")
    //private Usuario dueño = new Usuario();
    //private List<Usuario> colaboradores = new ArrayList<>();
    
    
}
