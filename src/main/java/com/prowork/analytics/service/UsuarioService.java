package com.prowork.analytics.service;

import com.prowork.analytics.model.Role;
import com.prowork.analytics.model.Usuario;
import com.prowork.analytics.repository.RoleRepository;
import com.prowork.analytics.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existeUsernameEnOtroUsuario(String username, Long idActual) {
        return usuarioRepository.findByUsername(username)
                .map(u -> !u.getId().equals(idActual))
                .orElse(false);
    }

    public void crearSupervisor(Usuario usuario) {

        if (usuario.getId() == null) {
            if (existeUsername(usuario.getUsername())) {
                throw new IllegalArgumentException("USERNAME_DUPLICADO");
            }
        } else {
            if (existeUsernameEnOtroUsuario(usuario.getUsername(), usuario.getId())) {
                throw new IllegalArgumentException("USERNAME_DUPLICADO");
            }
        }

        Role rolSupervisor = roleRepository.findByName("ROLE_SUPERVISOR")
                .orElseThrow(() -> new RuntimeException("ROLE_SUPERVISOR no existe"));

        if (usuario.getId() == null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.getRoles().add(rolSupervisor);
        }

        usuarioRepository.save(usuario);
    }

    public void crearOperario(Usuario usuario) {

        if (existeUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("USERNAME_DUPLICADO");
        }

        Role rolOperario = roleRepository.findByName("ROLE_OPERARIO")
                .orElseThrow(() -> new RuntimeException("ROLE_OPERARIO no existe"));

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.getRoles().add(rolOperario);

        usuarioRepository.save(usuario);
    }

    public List<Usuario> listarSupervisores() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_SUPERVISOR")))
                .toList();
    }

    public List<Usuario> listarOperarios() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_OPERARIO")))
                .toList();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
