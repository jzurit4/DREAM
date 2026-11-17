package cl.pzdream.auth.service;

import cl.pzdream.auth.dto.*;
import cl.pzdream.auth.model.Role;
import cl.pzdream.auth.model.Usuario;
import cl.pzdream.auth.repository.UsuarioRepository;
import cl.pzdream.auth.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthService(UsuarioRepository repo, PasswordEncoder encoder,
            AuthenticationManager authManager, JwtService jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwt = jwt;
    }

    @Transactional
    public UsuarioResponse register(RegisterRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Usuario u = new Usuario();
        u.setNombre(req.nombre());
        u.setEmail(req.email());
        u.setPasswordHash(encoder.encode(req.password()));
        u.setTelefono(req.telefono());
        u.setDireccion(req.direccion());
        u.setRole(Role.ROLE_CLIENTE);
        return UsuarioResponse.from(repo.save(u));
    }

    public AuthResponse login(LoginRequest req) {
        // Autentica con Spring Security
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.identifier(), req.password()));

        // Carga el usuario y emite JWT
        Usuario u = repo.findByEmail(req.identifier())
                .or(() -> repo.findByUsername(req.identifier()))
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String token = jwt.generate(u.getEmail(), u.getRole().name());
        return new AuthResponse(token, UsuarioResponse.from(u));
    }

    public UsuarioResponse me(String email) {
        return repo.findByEmail(email)
                .map(UsuarioResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}