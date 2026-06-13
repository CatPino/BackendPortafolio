package backend_notificaciones.service;

import java.util.Map;

public interface INotificacionServicio {
    void enviarConfirmacionCompra(Map<String, Object> payload);
    void enviarMensajeContacto(Map<String, Object> payload);
}