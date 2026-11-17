package cl.pzdream.auth.dto;

public record AuthResponse(
        String token,
        UsuarioResponse user) {
}