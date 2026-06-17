package backend_notificaciones.service;

import java.util.Map;

public interface NotificacionService {
    void enviarConfirmacionCompra(Map<String, Object> payload);
    void enviarMensajeContacto(Map<String, Object> payload);
}