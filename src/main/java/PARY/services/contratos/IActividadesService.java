package PARY.services.contratos;

import PARY.entity.misActividades;

import java.util.List;

public interface IMisActividadesService extends IGenericsService<misActividades>{
    List<misActividades> findActividadesByNombreContains(String nombre);

    List<misActividades> showRecentAct();

    void deleteOldAct();

}
