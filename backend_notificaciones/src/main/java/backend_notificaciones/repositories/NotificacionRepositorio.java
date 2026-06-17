package backend_notificaciones.repositories;

import backend_notificaciones.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NotificacionRepositorio extends JpaRepository<Notificacion, UUID> {}