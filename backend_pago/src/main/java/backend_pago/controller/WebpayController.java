package backend_pago.controller;

import backend_pago.dto.PagoRequest;
import backend_pago.service.webpayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webpay")
public class WebpayController {

    private final webpayService webpayService;

    public WebpayController(webpayService webpayService) {
        this.webpayService = webpayService;
    }

    @PostMapping("/crear")
    public Map<String, Object> crearTransaccion(@RequestBody PagoRequest request) {
        return webpayService.crearTransaccion(request);
    }

    @GetMapping("/confirmar")
    public ResponseEntity<Void> confirmarPago(
            @RequestParam("token_ws") String token,
            @RequestParam("sid") String sid) {
        String redirectUrl = webpayService.confirmarPago(token, sid);
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", redirectUrl)
            .build();
    }
}