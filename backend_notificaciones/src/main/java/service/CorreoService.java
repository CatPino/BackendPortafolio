package backend_notificaciones.service;

public interface CorreoServicio {
    void enviarCorreo(String para, String asunto, String cuerpo);
}