package cl.pzdream.auth.dto;

import cl.pzdream.auth.model.Role;
import cl.pzdream.auth.model.Usuario;

import java.time.Instant;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        String username,
        Role role,
        String telefono,
        String direccion,
        Boolean activo,
        Instant createdAt) {
    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getUsername(),
                u.getRole(),
                u.getTelefono(),
                u.getDireccion(),
                u.getActivo(),
                u.getCreatedAt());
    }
}