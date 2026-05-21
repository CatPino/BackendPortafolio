package backend_pago.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend_pago.repositories.pagoRepository;
import backend_pago.entities.Boleta;
import backend_pago.entities.DetalleBoleta;
import backend_pago.entities.Pago;
import backend_pago.repositories.boletaRepository;
import backend_pago.repositories.detalleBoletaRepository;

@Service
public class pagoServiceImpl implements pagoService {

    @Autowired
    private pagoRepository pagoRepository;

    @Autowired
    private boletaRepository boletaRepository;

    @Autowired
    private detalleBoletaRepository detalleBoletaRepository;

    @Override
    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    @Override
    public Pago obtenerPorId(Long idPago) {
        return pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
    }

    @Override
    public Pago crearPago(Pago pago, Boleta boleta, List<DetalleBoleta> detalles) {

        double subtotal = detalles.stream()
                .mapToDouble(DetalleBoleta::getSubtotal)
                .sum();

        double total = subtotal;
        double iva = Math.round(subtotal * (19.0 / 119.0));
        double neto = subtotal - iva;

        pago.setTotal(total);
        pago.setIva(iva);
        pago.setSubtotal(neto);
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }

        Pago pagoGuardado = pagoRepository.save(pago);

        boleta.setPago(pagoGuardado);
        Boleta boletaGuardada = boletaRepository.save(boleta);

        for (DetalleBoleta detalle : detalles) {
            detalle.setBoleta(boletaGuardada);
            detalleBoletaRepository.save(detalle);
        }

        return pagoGuardado;
    }

        @Override
        public void eliminarPago(Long idPago) {
            Pago pago = obtenerPorId(idPago);
            pagoRepository.delete(pago);
        }

        public Boleta obtenerBoletaPorId(Long id) {
        return boletaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Boleta no encontrada"));
    }
}