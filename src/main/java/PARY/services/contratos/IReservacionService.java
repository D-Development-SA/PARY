package PARY.services.contratos;

import PARY.entity.Reservacion;

import java.util.List;

public interface IReservacionService extends IGenericsService<Reservacion>{
    void deleteAll(List<Reservacion> reser);
}
