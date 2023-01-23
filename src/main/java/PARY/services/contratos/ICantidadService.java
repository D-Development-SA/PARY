package PARY.services.contratos;

import PARY.entity.pktAct.Cantidad;

import java.util.List;

public interface ICantidadService extends IGenericsService<Cantidad>{
    List<Cantidad> findCantidadesByCantReserv(int cantReserv);
    List<Cantidad> findCantidadesByCantMeEncanta(int cantMeEncanta);
}
