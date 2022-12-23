package PARY.services.contratos;

import PARY.entity.MisActividades;

import java.util.List;

public interface IMisActividadesService extends IGenericsService<MisActividades>{
    List<MisActividades> findActividadesByNombreContains(String nombre);

    List<MisActividades> showRecentAct();

    void deleteOldAct();

}
