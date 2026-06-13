package backend_notificaciones.service;

import Backend_notificaciones.entities.Notificacion;
import Backend_notificaciones.repositories.NotificacionRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificacionServicio {

    private final NotificacionRepositorio notificacionRepositorio;
    private final CorreoServicio correoServicio;

    public void enviarConfirmacionCompra(Map<String, Object> payload) {
        String correo = (String) payload.get("correo_comprador");
        String nombre = (String) payload.get("nombre_comprador");
        String ordenId = (String) payload.get("orden_id");

        String asunto = "¡Tu compra fue exitosa!";
        String cuerpo = "Hola " + nombre + ",\n\nTu pedido #" + ordenId + " ha sido confirmado.\n\n¡Gracias por tu compra!";

        Notificacion notificacion = new Notificacion();
        notificacion.setTipo("confirmacion_compra");
        notificacion.setCorreoDestinatario(correo);
        notificacion.setAsunto(asunto);
        notificacion.setCuerpo(cuerpo);
        notificacion.setReferenciaId(ordenId);

        try {
            correoServicio.enviarCorreo(correo, asunto, cuerpo);
            notificacion.setEstado("enviado");
            notificacion.setEnviadoEn(LocalDateTime.now());
        } catch (Exception e) {
            notificacion.setEstado("fallido");
        }

        notificacionRepositorio.save(notificacion);
    }

    public void enviarMensajeContacto(Map<String, Object> payload) {
        String nombre = (String) payload.get("nombre");
        String correo = (String) payload.get("correo");
        String tipo = (String) payload.get("tipo");
        String mensaje = (String) payload.get("mensaje");

        String asunto = tipo.equals("reclamo") ? "Nuevo reclamo recibido" : "Nueva consulta recibida";
        String cuerpo = "De: " + nombre + " (" + correo + ")\n\n" + mensaje;
        String correoAdmin = "admin@tudominio.com";

        Notificacion notificacion = new Notificacion();
        notificacion.setTipo("contacto_" + tipo);
        notificacion.setCorreoDestinatario(correoAdmin);
        notificacion.setAsunto(asunto);
        notificacion.setCuerpo(cuerpo);

        try {
            correoServicio.enviarCorreo(correoAdmin, asunto, cuerpo);
            notificacion.setEstado("enviado");
            notificacion.setEnviadoEn(LocalDateTime.now());
        } catch (Exception e) {
            notificacion.setEstado("fallido");
        }

        notificacionRepositorio.save(notificacion);
    }
}