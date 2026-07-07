package com.backend.backend_usuario.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.backend_usuario.entities.Contacto;
import com.backend.backend_usuario.repositories.ContactoRepository;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    public Contacto guardar(Contacto contacto) {
        contacto.setLeido(false);
        Contacto guardado = contactoRepository.save(contacto);

        enviarCorreoNotificacion(guardado);

        return guardado;
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

    private void enviarCorreoNotificacion(Contacto contacto) {
        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        String body = """
        {
        "sender": {
            "name": "Lumiskin - Formulario de contacto",
            "email": "soporte.lumiskin@gmail.com"
        },
        "to": [
            {
            "email": "soporte.lumiskin@gmail.com",
            "name": "Equipo Lumiskin"
            }
        ],
        "replyTo": {
            "email": "%s",
            "name": "%s"
        },
        "subject": "Nuevo mensaje de contacto de %s",
        "htmlContent": "<p><strong>Nombre:</strong> %s</p><p><strong>Correo:</strong> %s</p><p><strong>Mensaje:</strong></p><p>%s</p>"
        }
        """.formatted(
                contacto.getEmail(),
                contacto.getNombre(),
                contacto.getNombre(),
                contacto.getNombre(),
                contacto.getEmail(),
                contacto.getContenido()
        );

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println("Brevo response: " + response.getStatusCode());
    }
}