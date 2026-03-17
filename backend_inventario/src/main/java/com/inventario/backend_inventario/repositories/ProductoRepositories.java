package com.inventario.backend_inventario.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.inventario.backend_inventario.entities.Producto;

public interface ProductoRepositories extends CrudRepository <Producto, Long>{

    List<Producto> findByNombre(String nombre);
    List<Producto> findByCategoria_Nombre(String categoriaNombre);
    List<Producto> findByStockLessThan(int stock);
    List<Producto> findByActivoTrue();

}




