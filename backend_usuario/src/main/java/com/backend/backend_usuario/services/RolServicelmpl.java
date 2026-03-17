package com.backend.backend_usuario.services;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend_usuario.entities.Rol;
import com.backend.backend_usuario.repositories.RolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolServicelmpl {

    private final RolRepository rolRepository;

    @Transactional
    public Rol crear(String nombre, String descripcion) {
        String norm = normalizar(nombre);
        if (rolRepository.existeNombre(norm)) {
            throw new DataIntegrityViolationException("Ya existe un rol con ese nombre");
        }
        Rol r = new Rol();
        r.setNombre(norm);
        r.setDescripcion(descripcion != null ? descripcion.trim() : null);
        return rolRepository.save(r);
    }

    @Transactional(readOnly = true)
    public List<Rol> listar(String q) {
        List<Rol> roles = rolRepository.listarTodosOrdenNombre();
        if (q == null || q.isBlank()) return roles;
        String texto = q.trim().toLowerCase();
        return roles.stream()
                .filter(r -> r.getNombre() != null && r.getNombre().toLowerCase().contains(texto))
                .toList();
    }

    @Transactional(readOnly = true)
    public Rol obtener(Long id) {
        return rolRepository.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
    }

    @Transactional
    public Rol actualizar(Long id, String nombre, String descripcion) {
        Rol r = obtener(id);

        if (nombre != null && !nombre.isBlank()) {
            String nuevo = normalizar(nombre);
            if (!nuevo.equalsIgnoreCase(r.getNombre()) && rolRepository.existeNombre(nuevo)) {
                throw new DataIntegrityViolationException("Ya existe un rol con ese nombre");
            }
            r.setNombre(nuevo);
        }
        if (descripcion != null) r.setDescripcion(descripcion.trim());

        return rolRepository.save(r);
    }

    @Transactional
    public void eliminar(Long id) {
        Rol r = obtener(id);
        try {
            rolRepository.delete(r);
        } catch (DataIntegrityViolationException ex) {

            throw new DataIntegrityViolationException("No se puede eliminar el rol porque está en uso", ex);
        }
    }
    
    private String normalizar(String nombre) {
        return nombre == null ? null : nombre.trim().toLowerCase();
    }
}