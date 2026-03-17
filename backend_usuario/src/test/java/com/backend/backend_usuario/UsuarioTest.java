package com.backend.backend_usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.backend.backend_usuario.dto.SolicitudCrearUsuario;
import com.backend.backend_usuario.entities.Rol;
import com.backend.backend_usuario.entities.Usuario;
import com.backend.backend_usuario.repositories.RolRepository;
import com.backend.backend_usuario.repositories.UsuarioRepository;
import com.backend.backend_usuario.services.UsuarioServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("cliente");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Francisco");
        usuario.setEmail("pancho@correo.com");
        usuario.setPassword("12345678");
        usuario.setEstado(true);
        usuario.setRol(rol);
    }

    @Test
    void crearUsuario_exitoso() {
        SolicitudCrearUsuario req = new SolicitudCrearUsuario(
                "Francisco",
                "pancho@correo.com",
                "12345678",
                "999999999",
                "Maule",
                "Talca"
        );

        when(usuarioRepository.existsByEmail("pancho@correo.com")).thenReturn(false);
        when(passwordEncoder.encode("12345678")).thenReturn("encoded123");
        when(rolRepository.buscarPorNombre("cliente")).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.crear(req);

        assertNotNull(resultado);
        assertEquals("Francisco", resultado.getNombre());
        assertEquals("pancho@correo.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode("12345678");
        verify(rolRepository, times(1)).buscarPorNombre("cliente");
    }
    
    @Test
    void obtenerUsuarioPorId_existente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("Francisco", resultado.getNombre());
        assertTrue(resultado.isEstado());
        verify(usuarioRepository, times(1)).findById(1L);
    }
}
