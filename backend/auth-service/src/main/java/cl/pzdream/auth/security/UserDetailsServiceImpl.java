package cl.pzdream.auth.security;

import cl.pzdream.auth.model.Usuario;
import cl.pzdream.auth.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repo;

    public UserDetailsServiceImpl(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Acepta email o username
        Usuario u = repo.findByEmail(identifier)
                .or(() -> repo.findByUsername(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + identifier));

        return User.withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority(u.getRole().name())))
                .disabled(!u.getActivo())
                .build();
    }
}