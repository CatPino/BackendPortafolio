package backend_notificaciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CorreoServiceImpl implements CorreoService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    public void enviarCorreo(String para, String nombreDestinatario, String asunto, String cuerpo) {
        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        String body = """
        {
          "sender": {
            "name": "Lumiskin",
            "email": "soporte.lumiskin@gmail.com"
          },
          "to": [
            {
              "email": "%s",
              "name": "%s"
            }
          ],
          "subject": "%s",
          "htmlContent": "%s"
        }
        """.formatted(para, nombreDestinatario, asunto, cuerpo);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            System.out.println("Brevo response: " + response.getStatusCode());
        } catch (Exception e) {
            System.out.println("No se pudo enviar el correo: " + e.getMessage());
        }
    }
}