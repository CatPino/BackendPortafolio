package backend_pago.repositories;

import org.springframework.data.repository.CrudRepository;

import backend_pago.entities.Boleta;

public interface boletaRepository extends CrudRepository <Boleta, Long> {

}
