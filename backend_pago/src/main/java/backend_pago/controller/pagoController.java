package backend_pago.controller;

import backend_pago.dto.PagoRequest;
import backend_pago.entities.Boleta;
import backend_pago.entities.DetalleBoleta;
import backend_pago.entities.Pago;
import backend_pago.service.pagoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "Gestión de pagos y boletas")
public class pagoController {

    @Autowired
    private pagoService pagoService;

    @Operation(summary = "Preflight CORS para confirmar pago")
    @RequestMapping(value = "/confirmar", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> preflight() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Confirmar un pago y generar boleta")
    @PostMapping("/confirmar")
    public ResponseEntity<Map<String, Object>> confirmarPago(@RequestBody PagoRequest request) {

        Pago pago = new Pago();
        pago.setMetodoPago(request.getMetodoPago());

        Boleta boleta = new Boleta();
        boleta.setNombreCliente(request.getNombreCliente());
        boleta.setCorreoCliente(request.getCorreoCliente());
        boleta.setTelefonoCliente(request.getTelefonoCliente());

        boleta.setDireccionCliente(
                request.getDireccionCliente() + " " +
                request.getComunaCliente() + " " +
                request.getRegionCliente()
        );

        boleta.setIndicacionesEnvio(request.getIndicacionesEnvio());
        boleta.setPago(pago);

        List<DetalleBoleta> detalles = new ArrayList<>();
        double subtotal = 0.0;

        for (var d : request.getDetalles()) {

            DetalleBoleta det = new DetalleBoleta();
            det.setProducto(d.getProducto());
            det.setCantidad(d.getCantidad());
            det.setPrecioUnitario(d.getPrecioUnitario());
            det.setImagenUrl(d.getImagenUrl());

            double sub = d.getCantidad() * d.getPrecioUnitario(); // precio YA incluye IVA
            det.setSubtotal(sub);
            subtotal += sub;

            det.setBoleta(boleta);
            detalles.add(det);
        }
     
        double iva = Math.round(subtotal * (19.0 / 119.0));
        double total = subtotal;        // total ya incluye IVA
        double neto = subtotal - iva;   // neto sin IVA (solo referencia, no se guarda)

        pago.setSubtotal(subtotal); 
        pago.setIva(iva);
        pago.setTotal(total);

        boleta.setDetalles(detalles);
        pago.setBoleta(boleta);

        Pago pagoGuardado = pagoService.crearPago(pago, boleta, detalles);

        Map<String, Object> response = new HashMap<>();
        response.put("pago", pagoGuardado);
        response.put("boleta", pagoGuardado.getBoleta());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener todos los pagos")
    @GetMapping
    public ResponseEntity<List<Pago>> obtenerTodosLosPagos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }
}
