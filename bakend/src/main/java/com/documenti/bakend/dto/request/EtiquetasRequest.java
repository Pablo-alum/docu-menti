package com.documenti.bakend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * EtiquetasRequest
 */
public record EtiquetasRequest(

    @NotBlank(message = "por favor poner un nombre a la Etiqueta")
    String nombre,
    @NotBlank(message = "por favor selecione un color")
    String color
        ) {
}
