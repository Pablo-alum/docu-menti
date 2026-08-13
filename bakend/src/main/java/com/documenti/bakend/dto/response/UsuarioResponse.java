package com.documenti.bakend.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * UsuarioResponse
 */
public record UsuarioResponse(
     UUID id,
     String nombre,
     LocalDateTime creadoEn,
     String correo
        ) {
}

