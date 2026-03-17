package backend_pago.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import backend_pago.entities.Pago;

public interface pagoRepository extends JpaRepository <Pago,Long>{

}
