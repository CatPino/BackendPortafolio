package backend_notificaciones.service;

import backend_notificaciones.entities.Notificacion;
import backend_notificaciones.repositories.NotificacionRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepositorio notificacionRepositorio;
    private final CorreoServiceImpl correoServicio;

    @Override
    public void enviarConfirmacionCompra(Map<String, Object> payload) {
        String correo = (String) payload.get("correo_comprador");
        String nombre = (String) payload.get("nombre_comprador");
        String ordenId = (String) payload.get("orden_id");

        String asunto = "¡Tu compra fue exitosa!";
        String cuerpo = "<p>Hola " + nombre + ",</p><p>Tu pedido #" + ordenId + " ha sido confirmado.</p><p>¡Gracias por tu compra!</p><p>Equipo Lumiskin</p>";

        Notificacion notificacion = new Notificacion();
        notificacion.setTipo("confirmacion_compra");
        notificacion.setCorreoDestinatario(correo);
        notificacion.setAsunto(asunto);
        notificacion.setCuerpo(cuerpo);
        notificacion.setReferenciaId(ordenId);

        try {
            correoServicio.enviarCorreo(correo, nombre, asunto, cuerpo);
            notificacion.setEstado("enviado");
            notificacion.setEnviadoEn(LocalDateTime.now());
        } catch (Exception e) {
            notificacion.setEstado("fallido");
        }

        notificacionRepositorio.save(notificacion);
    }

    @Override
    public void enviarMensajeContacto(Map<String, Object> payload) {
        String nombre = (String) payload.get("nombre");
        String correo = (String) payload.get("correo");
        String tipo = (String) payload.get("tipo");
        String mensaje = (String) payload.get("mensaje");

        String asunto = tipo.equals("reclamo") ? "Nuevo reclamo recibido" : "Nueva consulta recibida";
        String cuerpo = "<p>De: " + nombre + " (" + correo + ")</p><p>" + mensaje + "</p>";
        String correoAdmin = "soporte.lumiskin@gmail.com";

        Notificacion notificacion = new Notificacion();
        notificacion.setTipo("contacto_" + tipo);
        notificacion.setCorreoDestinatario(correoAdmin);
        notificacion.setAsunto(asunto);
        notificacion.setCuerpo(cuerpo);

        try {
            correoServicio.enviarCorreo(correoAdmin, "Administrador", asunto, cuerpo);
            notificacion.setEstado("enviado");
            notificacion.setEnviadoEn(LocalDateTime.now());
        } catch (Exception e) {
            notificacion.setEstado("fallido");
        }

        notificacionRepositorio.save(notificacion);
    }
}