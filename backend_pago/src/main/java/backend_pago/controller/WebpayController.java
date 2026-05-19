package backend_pago.controller;

import backend_pago.dto.DetalleBoletaRequest;
import backend_pago.dto.PagoRequest;
import backend_pago.entities.Boleta;
import backend_pago.entities.DetalleBoleta;
import backend_pago.entities.Pago;
import backend_pago.security.JwtUtils;
import backend_pago.service.pagoService;
import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCommitResponse;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCreateResponse;

import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/api/webpay")
@CrossOrigin(origins = "http://localhost:5173") // Ajusta el puerto según tu frontend
public class WebpayController {

    private final pagoService pagoService;
    private final WebpayPlus.Transaction transaction;
    private final Map<String, PagoRequest> pagosPendientes = new HashMap<>();
    private final JwtUtils jwtUtils;

    public WebpayController(pagoService pagoService, JwtUtils jwtUtils) {
        this.pagoService = pagoService;
        this.jwtUtils = jwtUtils;

        WebpayOptions options = new WebpayOptions(
                "597055555532", // tu commerce code
                "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C", // tu apiKey test
                IntegrationType.TEST
        );

        this.transaction = new WebpayPlus.Transaction(options);
    }

    @PostMapping("/crear")
    public Map<String, Object> crearTransaccion(@RequestBody PagoRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String buyOrder = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
            String sessionId = UUID.randomUUID().toString();
            String returnUrl = "https://backendportafolio-635z.onrender.com/api/webpay/confirmar"; 

            pagosPendientes.put(sessionId, request);

            int monto = (int) Math.round(request.getTotal());

            WebpayPlusTransactionCreateResponse tx = transaction.create(
                    buyOrder,
                    sessionId,
                    monto,
                    returnUrl
            );

            Map<String, Object> claims = new HashMap<>();
            claims.put("nombre", request.getNombreCliente());
            claims.put("correo", request.getCorreoCliente());
            claims.put("telefono", request.getTelefonoCliente());

            claims.put("direccion", request.getDireccionCliente());
            claims.put("region", request.getRegionCliente());
            claims.put("comuna", request.getComunaCliente());
            claims.put("indicaciones", request.getIndicacionesEnvio());

            claims.put("carrito", request.getDetalles());
            claims.put("total", request.getTotal());

            String jwt = jwtUtils.generateToken(claims);

            response.put("url", tx.getUrl() + "?token_ws=" + tx.getToken());
            response.put("token", tx.getToken());
            response.put("jwt", jwt);
            response.put("mensaje", "Transacción creada correctamente");

        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/confirmar")
    public Boleta confirmarPago(
            @RequestParam("token_ws") String token,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            // Extraer token JWT
            String jwt = authHeader.replace("Bearer ", "");
            Map<String, Object> dataCliente = jwtUtils.getClaims(jwt);

            WebpayPlusTransactionCommitResponse commit = transaction.commit(token);
            String sessionId = commit.getSessionId();

            PagoRequest request = pagosPendientes.get(sessionId);
            if (request == null)
                throw new RuntimeException("Pago no encontrado en sesión");

            // Crear entidad Pago
            Pago pago = new Pago();
            pago.setMetodoPago("WEBPAY");
            pago.setTotal(request.getTotal());

            // Crear boleta asociada
            Boleta boleta = new Boleta();
            String nombre = (String) dataCliente.get("nombre");
            boleta.setNombreCliente(nombre);
            boleta.setCorreoCliente((String) dataCliente.get("correo"));
            boleta.setDireccionCliente(
                dataCliente.get("direccion") + ", " +
                dataCliente.get("comuna") + ", " +
                dataCliente.get("region")
            );

            boleta.setPago(pago);

            List<DetalleBoleta> detalles = new ArrayList<>();
            for (DetalleBoletaRequest d : request.getDetalles()) {
                DetalleBoleta det = new DetalleBoleta();
                det.setProducto(d.getProducto());
                det.setCantidad(d.getCantidad());
                det.setPrecioUnitario(d.getPrecioUnitario());
                det.setSubtotal(d.getSubtotal());
                det.setBoleta(boleta);
                detalles.add(det);
            }

            boleta.setDetalles(detalles);
            pago.setBoleta(boleta);

            pagoService.crearPago(pago, boleta, detalles);

            pagosPendientes.remove(sessionId);

            return boleta;

        } catch (Exception e) {
            throw new RuntimeException("Error al confirmar pago: " + e.getMessage());
        }
    }
}
