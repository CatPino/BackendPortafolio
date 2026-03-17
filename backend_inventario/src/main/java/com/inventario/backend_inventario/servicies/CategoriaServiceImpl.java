package com.inventario.backend_inventario.servicies;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.backend_inventario.entities.Categoria;
import com.inventario.backend_inventario.repositories.CategoriaRepositories;


@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepositories categoriaRepositories;

    @Override
    public Categoria crear(Categoria categoria){
        return categoriaRepositories.save(categoria);
    }


    @Override
    public Categoria obtenerId(Long id) {
        return categoriaRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Override
    public List<Categoria> listarTodas() {
        return (List<Categoria>) categoriaRepositories.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (!categoriaRepositories.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepositories.deleteById(id);
    }

    @Override
    public Categoria actualizar(Long id, Categoria categoriaActualizada) {
        Categoria existente = obtenerId(id);
        existente.setNombre(categoriaActualizada.getNombre());
        return categoriaRepositories.save(existente);
    }


}