package com.documenti.bakend.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.documenti.bakend.enums.EstadoDocumento;

/**
 * DocumentoResponse
 */
public record DocumentoResponse(
     UUID id,
     String nombre,
     LocalDateTime nombreCreacion,
     long tamaño,
     EstadoDocumento estado,
     String cofre,
     String key

) {
}
