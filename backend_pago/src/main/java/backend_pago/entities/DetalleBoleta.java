package backend_pago.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalle_boleta")
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private String imagenUrl;

    // Relación muchos a uno con Boleta
    @ManyToOne
    @JoinColumn(name = "boleta_id", referencedColumnName = "idBoleta")
    private Boleta boleta;
}