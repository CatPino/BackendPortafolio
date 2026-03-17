package backend_pago.dto;

import lombok.Data;

@Data
public class DetalleBoletaRequest {
    private String producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private String imagenUrl;
}
