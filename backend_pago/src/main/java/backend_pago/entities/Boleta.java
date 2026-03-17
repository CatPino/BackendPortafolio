package backend_pago.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boleta")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBoleta;

    private String nombreCliente;
    private String correoCliente;
    private String telefonoCliente;
    private String direccionCliente;
    private String indicacionesEnvio;
    private LocalDateTime fechaEmision = LocalDateTime.now();

    // Relación 1:1 con Pago
    @OneToOne
    @JoinColumn(name = "pago_id", referencedColumnName = "idPago")
    @JsonIgnoreProperties("boleta")  
    @ToString.Exclude            
    @EqualsAndHashCode.Exclude     
    private Pago pago;

    // Relación 1:N con DetalleBoleta
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("boleta") 
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleBoleta> detalles;
}
