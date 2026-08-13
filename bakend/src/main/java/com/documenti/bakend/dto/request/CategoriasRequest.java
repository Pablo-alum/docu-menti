package com.documenti.bakend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * CategoriasRequest
 */
public record CategoriasRequest(
    @NotBlank(message = "por favor poner un nombre a la Categoria")
    String nombre

) {
}
