package com.backend.backend_usuario.services;

import java.util.List;

import com.backend.backend_usuario.entities.Rol;

public interface RolService {
    Rol crear(String nombre, String descripcion);
    List<Rol> listar(String q);
    Rol obtener(Long id);
    Rol actualizar(Long id, String nombre, String descripcion);
    void eliminar(Long id);
    String normalizar(String nombre);
}


