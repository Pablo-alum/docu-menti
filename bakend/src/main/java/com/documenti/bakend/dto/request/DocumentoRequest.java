package com.documenti.bakend.dto.request;

import java.time.LocalDateTime;

import com.documenti.bakend.enums.EstadoDocumento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DocumentoRequest
 */
public record DocumentoRequest(
    @NotBlank(message = "Por favor poner un nombre al documento")
    String nombre,
    @NotBlank(message = "Por favor poner un nombre al documento")
    LocalDateTime nombreCreacion,
    @NotBlank(message = "el documento no pude estar vacio")
    @Size(min = 10, message = "por favor no poner un documento vacio")
    long tamaño,
    EstadoDocumento estado,
    @NotBlank(message = "Error al guardar el documento")
    String cofre,
    @NotBlank(message = "Error al encriptar el documento")
    String key


) {
}
