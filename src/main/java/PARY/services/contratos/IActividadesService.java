package PARY.services.contratos;

import PARY.entity.Actividad;

import java.util.List;

public interface IActividadesService extends IGenericsService<Actividad>{
    List<Actividad> findActividadesByNombreContains(String nombre);

    List<Actividad> showRecentAct();

    void deleteOldAct();

}
