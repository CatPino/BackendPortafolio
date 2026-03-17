package com.backend.backend_usuario.services;

import java.util.List;

import com.backend.backend_usuario.dto.ActualizarPerfil;
import com.backend.backend_usuario.dto.ActualizarUsuarioAdmin;
import com.backend.backend_usuario.dto.SolicitudCrearUsuario;
import com.backend.backend_usuario.entities.Usuario;

public interface UsuarioService {
    Usuario crear(SolicitudCrearUsuario req);
    Usuario actualizarPerfil(Long id, ActualizarPerfil req);
    Usuario obtenerPorId(Long id);
    Usuario buscarPorEmail(String email);
    boolean verificarPassword(String passwordPlano, String passwordHash);
    List<Usuario> listarTodos();
    Usuario actualizar(Long id, ActualizarUsuarioAdmin req);
    void eliminar(Long id);
    Usuario desactivar(Long id);
    List<Usuario> listarActivos();
    List<Usuario> listarInactivos();
}


