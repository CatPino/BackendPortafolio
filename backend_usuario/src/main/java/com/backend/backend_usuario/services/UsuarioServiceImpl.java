package com.backend.backend_usuario.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.backend_usuario.dto.SolicitudCrearUsuario;
import com.backend.backend_usuario.dto.ActualizarPerfil;
import com.backend.backend_usuario.dto.ActualizarUsuarioAdmin;
import com.backend.backend_usuario.entities.Usuario;
import com.backend.backend_usuario.repositories.UsuarioRepository;
import com.backend.backend_usuario.repositories.RolRepository;
import com.backend.backend_usuario.entities.Rol;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    
   @Override
    public Usuario crear(SolicitudCrearUsuario req) {

    if (req.email() == null || req.email().isBlank()) {
        throw new RuntimeException("El correo electrónico no puede estar vacío.");
    }

    String email = req.email().trim().toLowerCase();

    if (usuarioRepository.existsByEmail(email)) {
        throw new RuntimeException("El correo electrónico ya está registrado.");
    }

    Usuario usuario = new Usuario();
    usuario.setNombre(req.nombre());
    usuario.setEmail(email);
    usuario.setPassword(passwordEncoder.encode(req.password()));
    usuario.setTelefono(req.telefono());
    usuario.setRegion(req.region());
    usuario.setComuna(req.comuna());
    usuario.setEstado(true);

    Rol rol = rolRepository.buscarPorNombre("cliente")
            .orElseThrow(() -> new RuntimeException("Rol 'cliente' no encontrado"));
    usuario.setRol(rol);

    return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarPerfil(Long id, ActualizarPerfil datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (datos.getNombre() != null) usuario.setNombre(datos.getNombre());
        if (datos.getTelefono() != null) usuario.setTelefono(datos.getTelefono());
        if (datos.getRegion() != null) usuario.setRegion(datos.getRegion());
        if (datos.getComuna() != null) usuario.setComuna(datos.getComuna());
        if (datos.getDireccion() != null) usuario.setDireccion(datos.getDireccion());
        if (datos.getDepartamento() != null) usuario.setDepartamento(datos.getDepartamento());
        if (datos.getInfoEnvio() != null) usuario.setInfoEnvio(datos.getInfoEnvio());

        return usuarioRepository.save(usuario);
    }


    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean verificarPassword(String passwordPlano, String passwordHash) {
        return passwordEncoder.matches(passwordPlano, passwordHash);
    }


    @Override
    public List<Usuario> listarTodos() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    public Usuario actualizar(Long id, ActualizarUsuarioAdmin req) {
        Usuario existente = obtenerPorId(id);

        if (req.nombre() != null) existente.setNombre(req.nombre());
        if (req.email() != null) existente.setEmail(req.email().trim().toLowerCase());
        if (req.password() != null) {
            existente.setPassword(passwordEncoder.encode(req.password())); 
        }
        if (req.telefono() != null) existente.setTelefono(req.telefono());
        if (req.region() != null) existente.setRegion(req.region());
        if (req.comuna() != null) existente.setComuna(req.comuna());
        if (req.estado() != null) existente.setEstado(req.estado());

        // Actualizar rol si se envía
        if (req.rolId() != null || (req.rolNombre() != null && !req.rolNombre().isBlank())) {
            Rol rol = (req.rolId() != null)
                    ? rolRepository.findById(req.rolId()).orElse(null)
                    : rolRepository.buscarPorNombre(req.rolNombre().trim()).orElse(null);
            if (rol != null) {
                existente.setRol(rol);
            }
        }

        return usuarioRepository.save(existente);
    }

    // ======================= ELIMINAR =======================
    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // ======================= DESACTIVAR =======================
    @Override
    public Usuario desactivar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEstado(false);
        return usuarioRepository.save(usuario);
    }

    // ======================= LISTAR ACTIVOS =======================
    @Override
    public List<Usuario> listarActivos() {
        return usuarioRepository.listarActivos();
    }

    // ======================= LISTAR INACTIVOS =======================
    @Override
    public List<Usuario> listarInactivos() {
        return usuarioRepository.listarInactivos();
    }
}