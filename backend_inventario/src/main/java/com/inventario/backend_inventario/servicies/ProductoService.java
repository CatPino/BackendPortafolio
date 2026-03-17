package com.inventario.backend_inventario.servicies;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inventario.backend_inventario.entities.Producto;


public interface ProductoService {

    Producto crear(Producto producto);
    Producto obtenerId(Long id);
    List<Producto> listarTodas();   
    void eliminar(Long id); 
    Producto actualizar(Long id, Producto productoActualizado);
    Producto desactivar(Long id);
    String subirImagen(Long idProducto, MultipartFile archivo);
    boolean validarStock(Long idProducto, int cantidadSolicitada);
    List<Producto> buscarPorCategoria(String categoriaNombre);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> listarStockBajo(); 
    List<Producto> listarActivos();
    void descontarStock(Long idProducto, int cantidad);

}
