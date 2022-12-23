package PARY.DAO;

import PARY.entity.Reservacion;
import org.springframework.data.repository.CrudRepository;

public interface IReservacionDAO extends CrudRepository<Reservacion, Long> {
}
