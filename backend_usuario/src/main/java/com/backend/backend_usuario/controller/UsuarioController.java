package com.backend.backend_usuario.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_usuario.dto.ActualizarPerfil;
import com.backend.backend_usuario.dto.ActualizarUsuarioAdmin;
import com.backend.backend_usuario.dto.SolicitudCrearUsuario;
import com.backend.backend_usuario.entities.Usuario;
import com.backend.backend_usuario.security.JwtUtil;
import com.backend.backend_usuario.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios, login y administración")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody SolicitudCrearUsuario req) {
        Usuario nuevo = usuarioService.crear(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Iniciar sesión y obtener un token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email y password son requeridos"));
        }

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        if (!usuarioService.verificarPassword(password, usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        if (!usuario.isEstado()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Usuario inactivo"));
        }

        String token = jwtUtil.generateToken(usuario);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("token", token);
        respuesta.put("id", usuario.getId());
        respuesta.put("nombre", usuario.getNombre());
        respuesta.put("email", usuario.getEmail());
        respuesta.put("telefono", usuario.getTelefono());
        respuesta.put("region", usuario.getRegion());
        respuesta.put("comuna", usuario.getComuna());
        respuesta.put("direccion", usuario.getDireccion());
        respuesta.put("departamento", usuario.getDepartamento());
        respuesta.put("infoEnvio", usuario.getInfoEnvio());
        respuesta.put("rol", usuario.getRol().getNombre());
        respuesta.put("estado", usuario.isEstado());

        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Actualizar usuario (modo administrador)")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioAdmin req) {

        Usuario actualizado = usuarioService.actualizar(id, req);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desactivar usuario (cambiar estado)")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable Long id) {
        Usuario desactivado = usuarioService.desactivar(id);
        return ResponseEntity.ok(desactivado);
    }

    @Operation(summary = "Listar usuarios activos")
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> listarActivos() {
        List<Usuario> activos = usuarioService.listarActivos();
        return ResponseEntity.ok(activos);
    }

    @Operation(summary = "Listar usuarios inactivos")
    @GetMapping("/inactivos")
    public ResponseEntity<List<Usuario>> listarInactivos() {
        List<Usuario> inactivos = usuarioService.listarInactivos();
        return ResponseEntity.ok(inactivos);
    }

    @Operation(summary = "Actualizar perfil del usuario (autogestión)")
    @PatchMapping("/{id}/perfil")
    public ResponseEntity<Usuario> actualizarPerfil(
            @PathVariable Long id,
            @RequestBody ActualizarPerfil req
    ) {
        Usuario actualizado = usuarioService.actualizarPerfil(id, req);
        return ResponseEntity.ok(actualizado);
    }
}
