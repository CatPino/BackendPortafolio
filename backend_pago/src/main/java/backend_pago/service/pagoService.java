package backend_pago.service;

import java.util.List;
import backend_pago.entities.Boleta;
import backend_pago.entities.DetalleBoleta;
import backend_pago.entities.Pago;

public interface pagoService {
    
    // Obtener todos los pagos
    List<Pago> obtenerTodos();
    
    // Obtener un pago por su ID
    Pago obtenerPorId(Long idPago);
    
    // Crear un nuevo pago junto con su boleta y detalles
    Pago crearPago(Pago pago, Boleta boleta, List<DetalleBoleta> detalles);
    
    // Eliminar un pago
    void eliminarPago(Long idPago);
}