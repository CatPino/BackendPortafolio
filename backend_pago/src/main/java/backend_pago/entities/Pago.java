package backend_pago.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    private String metodoPago;
    private Double subtotal;
    private Double iva;
    private Double total;
    private LocalDateTime fechaPago = LocalDateTime.now();

    // Relación 1:1 con Boleta (bidireccional)
    @OneToOne(mappedBy = "pago", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("pago")
    @ToString.Exclude               // ⛔ Evita recursión infinita en toString()
    @EqualsAndHashCode.Exclude      // ⛔ Evita recursión infinita en equals/hashCode
    private Boleta boleta;
}