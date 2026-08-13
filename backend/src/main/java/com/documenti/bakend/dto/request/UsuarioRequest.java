package com.documenti.bakend.dto.request;

import java.time.LocalDateTime;

import com.documenti.bakend.enums.SeguridadTipo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * UsarioRequest
 */
public record UsuarioRequest (
    @NotBlank(message ="Por favor por un nombre")
    String nombre,
    @NotBlank(message ="Por favor por un Contraseña")
    @Size(min = 8, message ="por favor pone una contraseña de  minimo 8 caracteres")
    String contraseña,
    @NotBlank(message = "Error a la hora de la creacion")
    LocalDateTime creadoEn,
    @NotBlank(message = "por favor ponga un correo valido")
    @Email
    String correo,

    SeguridadTipo tipoDeSeguridad
) {
}
