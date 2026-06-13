package backend_notificaciones.controller;

import Backend_notificaciones.service.NotificacionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionControlador {

    private final NotificacionServicio notificacionServicio;

    @PostMapping("/confirmacion-compra")
    public ResponseEntity<String> confirmacionCompra(@RequestBody Map<String, Object> payload) {
        notificacionServicio.enviarConfirmacionCompra(payload);
        return ResponseEntity.ok("Notificación de compra enviada");
    }

    @PostMapping("/contacto")
    public ResponseEntity<String> contacto(@RequestBody Map<String, Object> payload) {
        notificacionServicio.enviarMensajeContacto(payload);
        return ResponseEntity.ok("Mensaje de contacto recibido");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}