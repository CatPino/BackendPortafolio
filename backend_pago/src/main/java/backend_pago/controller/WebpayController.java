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
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/webpay")
@CrossOrigin(origins = "https://frontend-portafolio-lumiskin-yebo.vercel.app")
public class WebpayController {

    private final pagoService pagoService;
    private final WebpayPlus.Transaction transaction;
    private final JwtUtils jwtUtils;
    HttpServletResponse httpResponse;

    // ✅ Ambos Maps declarados correctamente como campos
    private final Map<String, PagoRequest> pagosPendientes = new HashMap<>();
    private final Map<String, String> jwtsPendientes = new HashMap<>();

    public WebpayController(pagoService pagoService, JwtUtils jwtUtils) {
        this.pagoService = pagoService;
        this.jwtUtils = jwtUtils;

        WebpayOptions options = new WebpayOptions(
                "597055555532",
                "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C",
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

            String returnUrl = "https://backend-pago.onrender.com/api/webpay/confirmar?sid=" + sessionId;

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

            pagosPendientes.put(sessionId, request);
            jwtsPendientes.put(sessionId, jwt);

            int monto = (int) Math.round(request.getTotal());

            WebpayPlusTransactionCreateResponse tx = transaction.create(
                    buyOrder,
                    sessionId,
                    monto,
                    returnUrl
            );

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
    public ResponseEntity<Void> confirmarPago(
            @RequestParam("token_ws") String token,
            @RequestParam("sid") String sid       
    ) {
        try {
            String jwt = jwtsPendientes.get(sid);
            if (jwt == null)
                throw new RuntimeException("JWT no encontrado para la sesión");

            Map<String, Object> dataCliente = jwtUtils.getClaims(jwt);

            WebpayPlusTransactionCommitResponse commit = transaction.commit(token);
            String sessionId = commit.getSessionId();

            PagoRequest request = pagosPendientes.get(sessionId);
            if (request == null)
                throw new RuntimeException("Pago no encontrado en sesión");

            Pago pago = new Pago();
            pago.setMetodoPago("WEBPAY");
            pago.setTotal(request.getTotal());

            Boleta boleta = new Boleta();
            boleta.setNombreCliente((String) dataCliente.get("nombre"));
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
            pagosPendientes.remove(sid);
            jwtsPendientes.remove(sid);

            String redirectUrl = "https://frontend-portafolio-lumiskin-yebo.vercel.app/compra-exitosa?idBoleta=" + boleta.getIdBoleta();
            return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build();

        } catch (Exception e) {
            throw new RuntimeException("Error al confirmar pago: " + e.getMessage());
        }
    }
}