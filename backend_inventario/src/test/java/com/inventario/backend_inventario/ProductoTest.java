package com.inventario.backend_inventario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import com.inventario.backend_inventario.entities.Categoria;
import com.inventario.backend_inventario.entities.Producto;
import com.inventario.backend_inventario.repositories.ProductoRepositories;
import com.inventario.backend_inventario.servicies.ProductoServiceImpl; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductoServiceImplTest {

    @Mock
    private ProductoRepositories productoRepositories;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Poleras");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Polera Oversize");
        producto.setDescripcion("Polera de algodón 100% color negro");
        producto.setPrecio(15990L);
        producto.setStock(20L);
        producto.setActivo(true);
        producto.setCategoria(categoria);
    }

    @Test
    void crearProducto_exitoso() {
        when(productoRepositories.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.crear(producto);

        assertNotNull(resultado);
        assertEquals("Polera Oversize", resultado.getNombre());
        verify(productoRepositories, times(1)).save(any(Producto.class));
    }

    @Test
    void obtenerProductoPorId_existente() {
        when(productoRepositories.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Polera Oversize", resultado.getNombre());
        verify(productoRepositories, times(1)).findById(1L);
    }

    @Test
    void listarProductosActivos() {
        when(productoRepositories.findByActivoTrue()).thenReturn(Arrays.asList(producto));

        List<Producto> activos = productoService.listarActivos();

        assertFalse(activos.isEmpty());
        assertEquals(1, activos.size());
        assertEquals("Polera Oversize", activos.get(0).getNombre());
        verify(productoRepositories, times(1)).findByActivoTrue();
    }
}