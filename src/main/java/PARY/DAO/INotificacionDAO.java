package PARY.DAO;

import PARY.entity.Notificacion;
import org.springframework.data.repository.CrudRepository;

public interface INotificacionDAO extends CrudRepository<Notificacion, Long> {
}
