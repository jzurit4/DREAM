package cl.pzdream.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El identificador es obligatorio") String identifier,
        @NotBlank(message = "La contraseña es obligatoria") String password) {
}