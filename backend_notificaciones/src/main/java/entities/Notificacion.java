package backend_notificaciones.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificaciones")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tipo; // "confirmacion_compra", "consulta_contacto", "reclamo_contacto"

    @Column(nullable = false)
    private String correoDestinatario;

    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String cuerpo;

    @Column(nullable = false)
    private String estado; // "pendiente", "enviado", "fallido"

    private String referenciaId; // order_id u otro

    private LocalDateTime creadoEn;
    private LocalDateTime enviadoEn;

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
        this.estado = "pendiente";
    }
}