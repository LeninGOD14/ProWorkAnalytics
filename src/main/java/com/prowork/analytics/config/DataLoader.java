package com.prowork.analytics.config;

import com.prowork.analytics.model.Role;
import com.prowork.analytics.model.Usuario;
import com.prowork.analytics.repository.RoleRepository;
import com.prowork.analytics.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        crearRolSiNoExiste("ROLE_ADMIN");
        crearRolSiNoExiste("ROLE_SUPERVISOR");
        crearRolSiNoExiste("ROLE_OPERARIO");

        crearAdminSiNoExiste();
    }

    private void crearRolSiNoExiste(String nombreRol) {
        roleRepository.findByName(nombreRol)
                .orElseGet(() -> {
                    Role rol = new Role();
                    rol.setName(nombreRol);
                    return roleRepository.save(rol);
                });
    }

    private void crearAdminSiNoExiste() {

        if (usuarioRepository.findByUsername("admin").isPresent()) {
            return;
        }

        Role rolAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN no existe"));

        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.getRoles().add(rolAdmin);

        usuarioRepository.save(admin);
    }
}
