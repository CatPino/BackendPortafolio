package com.inventario.backend_inventario.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inventario.backend_inventario.entities.Producto;
import com.inventario.backend_inventario.servicies.ProductoService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestión de productos del inventario")
public class ProductoRestControllers {

    @Autowired
    private ProductoService productoServices;

    @Operation(summary = "Crear un producto")
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoServices.crear(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    @Operation(summary = "Obtener un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoServices.obtenerId(id);
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoServices.listarTodas();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Eliminar un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar un producto")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id,
            @Valid @RequestBody Producto productoActualizado) {
        Producto producto = productoServices.actualizar(id, productoActualizado);
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Desactivar un producto")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Producto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(productoServices.desactivar(id));
    }

    @Operation(summary = "Validar stock disponible de un producto")
    @GetMapping("/{id}/validar")
    public ResponseEntity<Integer> validarStock(@PathVariable("id") Long idProducto) {
        Producto producto = productoServices.obtenerId(idProducto);

        if (producto == null || producto.getStock() <= 0) {
            return ResponseEntity.ok(0);
        }

        return ResponseEntity.ok(producto.getStock().intValue());
    }

    @Operation(summary = "Subir imagen de un producto")
    @PostMapping("/{id}/imagen")
    public ResponseEntity<?> subirImagen(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) {

        try {
            if (archivo == null || archivo.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Archivo vacío o no enviado (campo debe llamarse 'archivo')"));
            }

            String url = productoServices.subirImagen(id, archivo);
            return ResponseEntity.ok(Map.of("imagenUrl", url));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado: " + id));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Error inesperado"));
        }
    }

    @Operation(summary = "Buscar productos por nombre o categoría")
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscar(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria) {

        List<Producto> productos;

        if (nombre != null && !nombre.trim().isEmpty()) {
            productos = productoServices.buscarPorNombre(nombre);

        } else if (categoria != null && !categoria.trim().isEmpty()) {
            productos = productoServices.buscarPorCategoria(categoria);

        } else {
            productos = productoServices.listarTodas();
        }

        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener productos con stock bajo")
    @GetMapping("/alertas-stock")
    public ResponseEntity<List<Producto>> obtenerProductosConStockBajo() {
        List<Producto> productos = productoServices.listarStockBajo();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Descontar stock de un producto")
    @PatchMapping("/{id}/descontar")
    public ResponseEntity<?> descontarStock(
            @PathVariable Long id,
            @RequestParam int cantidad) {

        productoServices.descontarStock(id, cantidad);
        return ResponseEntity.ok(Map.of(
            "idProducto", id,
            "cantidadDescontada", cantidad
        ));
    }
}
