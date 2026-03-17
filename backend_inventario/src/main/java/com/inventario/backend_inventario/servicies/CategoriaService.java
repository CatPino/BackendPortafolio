package com.inventario.backend_inventario.servicies;

import java.util.List;

import com.inventario.backend_inventario.entities.Categoria;

public interface CategoriaService {

    Categoria crear(Categoria categoria);
    Categoria obtenerId(Long id);
    List<Categoria> listarTodas();    
    void eliminar(Long id);
    Categoria actualizar(Long id, Categoria categoriaActualizada);

}
