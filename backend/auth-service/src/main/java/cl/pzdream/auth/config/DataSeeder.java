package cl.pzdream.auth.config;

import cl.pzdream.auth.model.Role;
import cl.pzdream.auth.model.Usuario;
import cl.pzdream.auth.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    CommandLineRunner seedUsers(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setEmail("admin@pzdream.cl");
                admin.setUsername("admin");
                admin.setPasswordHash(encoder.encode("admin123"));
                admin.setRole(Role.ROLE_ADMIN);
                admin.setActivo(true);
                repo.save(admin);
                log.info("Usuario admin creado: admin / admin123");
            }

            // Cliente de ejemplo para probar login del frontend
            if (repo.findByEmail("cliente@pzdream.cl").isEmpty()) {
                Usuario cliente = new Usuario();
                cliente.setNombre("Cliente Demo");
                cliente.setEmail("cliente@pzdream.cl");
                cliente.setPasswordHash(encoder.encode("cliente123"));
                cliente.setTelefono("+56 9 1111 2222");
                cliente.setDireccion("Av. Siempre Viva 742, Santiago");
                cliente.setRole(Role.ROLE_CLIENTE);
                cliente.setActivo(true);
                repo.save(cliente);
                log.info("Cliente demo creado: cliente@pzdream.cl / cliente123");
            }
        };
    }
}