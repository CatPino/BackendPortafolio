package com.backend.backend_usuario.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend_usuario.entities.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    List<Contacto> findAllByOrderByIdDesc();

    List<Contacto> findByLeidoFalseOrderByIdDesc();
}