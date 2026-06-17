package backend_notificaciones.service;

public interface CorreoService {
    void enviarCorreo(String para, String nombreDestinatario, String asunto, String cuerpo);
}