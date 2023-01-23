package PARY.services.contratos;

import PARY.entity.Actividad;

import java.util.List;

public interface IActividadesService extends IGenericsService<Actividad>{
    List<Actividad> findActividadesByNombreContains(String nombre);

    List<Actividad> showRecentAct();

    List<Actividad> deleteOldAct();

    void deleteAll(List<Actividad> actividads);

    List<Actividad> findActividadesByProvinciaOrdenadas(String provincia);
}
