package com.backend.backend_usuario.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.backend_usuario.entities.Rol;

@Repository
public interface RolRepository extends CrudRepository<Rol, Long> {

    // ===== Básicos =====
    @Query("SELECT r FROM Rol r WHERE r.id = :id")
    Optional<Rol> obtenerPorId(@Param("id") Long id);  // alias en español (además de findById)

    @Query("SELECT r FROM Rol r ORDER BY r.nombre ASC")
    List<Rol> listarTodosOrdenNombre();

    // ===== Por nombre (case-insensitive) =====
    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombre) = LOWER(:nombre)")
    Optional<Rol> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT COUNT(r) > 0 FROM Rol r WHERE LOWER(r.nombre) = LOWER(:nombre)")
    boolean existeNombre(@Param("nombre") String nombre);

    // ===== Varios nombres =====
    @Query("SELECT r FROM Rol r WHERE r.nombre IN :nombres ORDER BY r.nombre ASC")
    List<Rol> listarPorNombres(@Param("nombres") List<String> nombres);
}