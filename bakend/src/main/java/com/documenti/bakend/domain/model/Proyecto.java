package com.documenti.bakend.domain.model;


import java.util.ArrayList;
import java.util.List;

import com.documenti.bakend.domain.model.base.BaseIdNombre;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Proyecto
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Proyecto extends BaseIdNombre  {
    @NotBlank(message =  "No hay dueño")

    private Usuario dueño = new Usuario();
    private List<Usuario> colaboradores = new ArrayList<>();

    
}
