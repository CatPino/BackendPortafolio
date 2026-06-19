package com.backend.backend_usuario.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend_usuario.entities.Contacto;
import com.backend.backend_usuario.repositories.ContactoRepository;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    public Contacto guardar(Contacto contacto) {
        contacto.setLeido(false);
        return contactoRepository.save(contacto);
    }

    public List<Contacto> listar() {
        return contactoRepository.findAllByOrderByIdDesc();
    }

    public Contacto marcarComoLeido(Long id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje de contacto no encontrado"));

        contacto.setLeido(true);
        return contactoRepository.save(contacto);
    }

    public void eliminar(Long id) {
        if (!contactoRepository.existsById(id)) {
            throw new RuntimeException("Mensaje de contacto no encontrado");
        }

        contactoRepository.deleteById(id);
    }
}