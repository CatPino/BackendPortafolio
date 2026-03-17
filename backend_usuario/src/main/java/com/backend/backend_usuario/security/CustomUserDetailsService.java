package com.backend.backend_usuario.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.backend_usuario.entities.Usuario;
import com.backend.backend_usuario.repositories.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : "cliente";

        GrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + rolNombre.toUpperCase());

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.isEstado(), 
                true,
                true,
                true,
                List.of(authority)
        );
    }
}