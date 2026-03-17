package com.backend.backend_usuario.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_usuario.entities.Rol;
import com.backend.backend_usuario.services.RolServicelmpl;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Gestión de roles de usuario")
public class RolController {

    private final RolServicelmpl rolService;

    @Operation(summary = "Crear un rol")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Rol req) {
        try {
            Rol nuevo = rolService.crear(req.getNombre(), req.getDescripcion());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Listar todos los roles")
    @GetMapping
    public ResponseEntity<List<Rol>> listar(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(rolService.listar(q));
    }

    @Operation(summary = "Obtener rol por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            Rol rol = rolService.obtener(id);
            return ResponseEntity.ok(rol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar rol")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Rol req) {
        try {
            Rol actualizado = rolService.actualizar(id, req.getNombre(), req.getDescripcion());
            return ResponseEntity.ok(actualizado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar rol")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            rolService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
