package backend_pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import backend_pago.entities.Boleta;
import backend_pago.entities.DetalleBoleta;
import backend_pago.entities.Pago;
import backend_pago.repositories.boletaRepository;
import backend_pago.repositories.detalleBoletaRepository;
import backend_pago.repositories.pagoRepository;
import backend_pago.service.pagoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class pagoServiceImplTest {

    @Mock
    private pagoRepository pagoRepository;

    @Mock
    private boletaRepository boletaRepository;

    @Mock
    private detalleBoletaRepository detalleBoletaRepository;

    @InjectMocks
    private pagoServiceImpl pagoService;

    private Pago pago;
    private Boleta boleta;
    private DetalleBoleta detalle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pago = new Pago();
        pago.setIdPago(1L);        
        pago.setMetodoPago("Tarjeta");

        boleta = new Boleta();
        boleta.setIdBoleta(1L);    
        boleta.setNombreCliente("Francisco");
        boleta.setCorreoCliente("pancho@correo.com");
        boleta.setTelefonoCliente("999999999");
        boleta.setDireccionCliente("Av. Siempre Viva 123 Talca Maule");

        detalle = new DetalleBoleta();
        detalle.setProducto("Polera Oversize");
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(15990.0);
        detalle.setSubtotal(31980.0);
    }

    @Test
    void crearPago_exitoso() {
        List<DetalleBoleta> detalles = Arrays.asList(detalle);

        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(boletaRepository.save(any(Boleta.class))).thenReturn(boleta);
        when(detalleBoletaRepository.save(any(DetalleBoleta.class))).thenReturn(detalle);

        Pago resultado = pagoService.crearPago(pago, boleta, detalles);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPago());
        assertEquals("Tarjeta", resultado.getMetodoPago());
        verify(pagoRepository, times(1)).save(any(Pago.class));
        verify(boletaRepository, times(1)).save(any(Boleta.class));
        verify(detalleBoletaRepository, times(1)).save(any(DetalleBoleta.class));
    }

    @Test
    void obtenerTodos_retornaLista() {
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago));

        List<Pago> resultado = pagoService.obtenerTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Tarjeta", resultado.get(0).getMetodoPago());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void obtenerBoletaPorId_existente() {
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boleta));

        Boleta resultado = pagoService.obtenerBoletaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Francisco", resultado.getNombreCliente());
        assertEquals("pancho@correo.com", resultado.getCorreoCliente());
        verify(boletaRepository, times(1)).findById(1L);
    }

}