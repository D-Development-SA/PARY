package PARY.DAO;

import PARY.entity.misActividades;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IMisActividadDAO extends CrudRepository<misActividades,Long> {

    List<misActividades> findActividadesByNombreContains(String nombre);

    List<misActividades> findActividadesByOrderByFechaHoraDesc();
}
