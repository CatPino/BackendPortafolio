package com.backend.backend_usuario.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.backend_usuario.entities.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    // ===== Básicos sobre email =====
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> buscarPorEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existeEmail(@Param("email") String email);

    // ===== Estado (boolean) =====
    @Query("SELECT u FROM Usuario u WHERE u.estado = true ORDER BY u.fechaCreacion DESC")
    List<Usuario> listarActivos();

    @Query("SELECT u FROM Usuario u WHERE u.estado = false ORDER BY u.fechaCreacion DESC")
    List<Usuario> listarInactivos();

    // ===== Búsqueda de texto en nombre o email =====
    @Query("""
           SELECT u FROM Usuario u
           WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
              OR LOWER(u.email)  LIKE LOWER(CONCAT('%', :texto, '%'))
           """)
    List<Usuario> buscarPorTexto(@Param("texto") String texto);

    // ===== Combinados por rol y estado =====
    @Query("""
           SELECT u FROM Usuario u
           WHERE u.rol.id = :rolId AND u.estado = :estado
           ORDER BY u.fechaCreacion DESC
           """)
    List<Usuario> listarPorRolYEstado(@Param("rolId") Long rolId, @Param("estado") boolean estado);

    // ===== Útiles =====
    @Query("SELECT u FROM Usuario u WHERE u.id = :id AND u.estado = true")
    Optional<Usuario> obtenerActivoPorId(@Param("id") Long id);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.estado = true")
    long contarActivos();

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.estado = false")
    long contarInactivos();

    Optional<Usuario> findByEmail(String email);

    // ✅ Saber si ya existe un correo registrado
    boolean existsByEmail(String email);
}