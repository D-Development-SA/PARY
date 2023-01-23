package PARY.DAO;

import PARY.entity.pktAct.Cantidad;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICantidadDAO extends CrudRepository<Cantidad, Long> {
    List<Cantidad> findCantidadesByCantReserv(int cantReserv);
    List<Cantidad> findCantidadesByCantMeEncanta(int cantMeEncanta);
}
