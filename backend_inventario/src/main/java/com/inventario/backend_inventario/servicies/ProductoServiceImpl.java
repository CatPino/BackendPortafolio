package com.inventario.backend_inventario.servicies;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.inventario.backend_inventario.entities.Producto;
import com.inventario.backend_inventario.repositories.ProductoRepositories;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepositories productoRepositories;

    @Override
    public Producto crear(Producto producto) {
        return productoRepositories.save(producto);
    }

    @Override
    public Producto obtenerId(Long id) {
        return productoRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public List<Producto> listarTodas() {
        return (List<Producto>) productoRepositories.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (!productoRepositories.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepositories.deleteById(id);
    }

    @Override

    public Producto actualizar(Long id, Producto productoActualizado) {
        Producto existente = obtenerId(id);
        existente.setNombre(productoActualizado.getNombre());
        existente.setDescripcion(productoActualizado.getDescripcion());
        existente.setPrecio(productoActualizado.getPrecio());
        existente.setStock(productoActualizado.getStock());
        existente.setCategoria(productoActualizado.getCategoria());
        existente.setActivo(productoActualizado.getActivo());
        return productoRepositories.save(existente);
    }

    @Override
    public Producto desactivar(Long id) {
        Producto producto = obtenerId(id);
        producto.setActivo(false);
        return productoRepositories.save(producto);
    }

    @Override
    public boolean validarStock(Long idProducto, int cantidadSolicitada) {
        Producto producto = obtenerId(idProducto);
        return producto != null && producto.getStock() >= cantidadSolicitada;
    }

    @Override
    public String subirImagen(Long idProducto, MultipartFile archivo) {
        try {
            if (archivo == null || archivo.isEmpty()) {
                throw new RuntimeException("Archivo vacío");
            }

            Path base = Paths.get(System.getProperty("user.dir"), "img");
            Files.createDirectories(base); 

            String original = archivo.getOriginalFilename();
            String ext = (original != null && original.contains("."))
                    ? original.substring(original.lastIndexOf("."))
                    : "";
            String nombre = "prod_" + idProducto + "_" + System.currentTimeMillis() + ext;

            Path destino = base.resolve(nombre);
            archivo.transferTo(destino.toFile());

            String url = "http://localhost:8081/img/" + nombre;

            Producto p = obtenerId(idProducto);
            p.setImagenUrl(url);
            productoRepositories.save(p);

            return url;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al subir imagen: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Producto> buscarPorCategoria(String categoriaNombre) {
        return productoRepositories.findByCategoria_Nombre(categoriaNombre);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepositories.findByNombre(nombre);
    }

    @Override
    public List<Producto> listarStockBajo() {
        return productoRepositories.findByStockLessThan(5);
    }

    @Override
    public List<Producto> listarActivos() {
        return productoRepositories.findByActivoTrue();
    }

    @Override
    public void descontarStock(Long idProducto, int cantidad) {
    Producto p = obtenerId(idProducto);

    if (p.getStock() < cantidad) {
        throw new RuntimeException("No hay stock suficiente");
    }
    p.setStock(p.getStock() - cantidad);
    productoRepositories.save(p);
}


}
