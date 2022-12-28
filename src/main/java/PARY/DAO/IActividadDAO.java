package PARY.DAO;

import PARY.entity.Actividad;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IActividadDAO extends CrudRepository<Actividad,Long> {

    List<Actividad> findActividadesByNombreContains(String nombre);

    List<Actividad> findActividadesByOrderByFechaHoraDesc();
}
