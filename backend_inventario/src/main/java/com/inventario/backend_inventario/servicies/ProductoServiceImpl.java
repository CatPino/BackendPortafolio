package com.inventario.backend_inventario.servicies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.inventario.backend_inventario.entities.Producto;
import com.inventario.backend_inventario.repositories.ProductoRepositories;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepositories productoRepositories;

    // Traemos las credenciales desde application.properties
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

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

    // --- AQUÍ ESTÁ LA MAGIA NUEVA ---
    @Override
    public String subirImagen(Long idProducto, MultipartFile archivo) {
        try {
            if (archivo == null || archivo.isEmpty()) {
                throw new RuntimeException("Archivo vacío");
            }

            // 1. Limpiar y armar el nombre del archivo
            String original = archivo.getOriginalFilename();
            String ext = (original != null && original.contains("."))
                    ? original.substring(original.lastIndexOf("."))
                    : "";
            // Reemplazamos espacios por guiones para evitar problemas en URLs
            String nombreLimpio = "prod_" + idProducto + "_" + System.currentTimeMillis() + ext;

            // 2. Preparar la URL de destino en la API de Supabase (bucket: inventario)
            String uploadUrl = supabaseUrl + "/storage/v1/object/inventario/" + nombreLimpio;

            // 3. Configurar los Headers con la clave de Supabase
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            // Tomamos el Content-Type original del archivo (ej. image/png)
            headers.setContentType(MediaType.valueOf(archivo.getContentType()));

            // 4. Armar la petición con los bytes del archivo
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(archivo.getBytes(), headers);
            RestTemplate restTemplate = new RestTemplate();

            // 5. Enviar el archivo a Supabase
            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl,
                    HttpMethod.POST, 
                    requestEntity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Error al subir a Supabase: " + response.getBody());
            }

            // 6. Construir la URL pública final
            String urlPublica = supabaseUrl + "/storage/v1/object/public/inventario/" + nombreLimpio;

            // 7. Guardar la URL correcta en la base de datos
            Producto p = obtenerId(idProducto);
            p.setImagenUrl(urlPublica);
            productoRepositories.save(p);

            return urlPublica;

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
