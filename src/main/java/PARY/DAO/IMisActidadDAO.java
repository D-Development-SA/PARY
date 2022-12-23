package PARY.DAO;

import PARY.entity.MisActividades;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IMisActidadDAO extends CrudRepository<MisActividades,Long> {

    List<MisActividades> findActividadesByNombreContains(String nombre);

    List<MisActividades> findActividadesByOrderByFechaHoraDesc();
}
