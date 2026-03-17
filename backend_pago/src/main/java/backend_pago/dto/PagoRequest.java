package backend_pago.dto;

import java.util.List;
import lombok.Data;

@Data
public class PagoRequest {

    private String nombreCliente;
    private String correoCliente;
    private String telefonoCliente;

    private String direccionCliente;
    private String regionCliente;
    private String comunaCliente;
    private String indicacionesEnvio;

    private Double total;
    private String metodoPago;

    private List<DetalleBoletaRequest> detalles;
}