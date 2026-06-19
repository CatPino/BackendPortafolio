package com.backend.backend_usuario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_usuario.entities.Contacto;
import com.backend.backend_usuario.services.ContactoService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "https://frontend-portafolio-lumiskin-yebo.vercel.app")
@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Contacto contacto) {
        try {
            Contacto nuevo = contactoService.guardar(contacto);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<List<Contacto>> listar() {
        return ResponseEntity.ok(contactoService.listar());
    }

    @PatchMapping("/{id}/leido")
    public ResponseEntity<?> marcarComoLeido(@PathVariable Long id) {
        try {
            Contacto contacto = contactoService.marcarComoLeido(id);
            return ResponseEntity.ok(contacto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            contactoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
}