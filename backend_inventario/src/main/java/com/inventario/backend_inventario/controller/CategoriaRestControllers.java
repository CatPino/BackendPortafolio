package com.inventario.backend_inventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.inventario.backend_inventario.entities.Categoria;
import com.inventario.backend_inventario.servicies.CategoriaService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Gestión de categorías del inventario")
public class CategoriaRestControllers {

    @Autowired
    private CategoriaService categoriaServices;

    @Operation(summary = "Crear una categoría")
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaServices.crear(categoria);
        return ResponseEntity.ok(nuevaCategoria);
    }

    @Operation(summary = "Obtener una categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = categoriaServices.obtenerId(id);
        return ResponseEntity.ok(categoria);
    }

    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = categoriaServices.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Eliminar una categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar una categoría")
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoriaActualizada) {
        Categoria categoria = categoriaServices.actualizar(id, categoriaActualizada);
        return ResponseEntity.ok(categoria);
    }
}
