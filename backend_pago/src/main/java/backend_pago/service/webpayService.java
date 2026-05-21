package backend_pago.service;

import backend_pago.dto.PagoRequest;
import java.util.Map;

public interface webpayService {
    Map<String, Object> crearTransaccion(PagoRequest request);
    String confirmarPago(String token, String sid);
}
