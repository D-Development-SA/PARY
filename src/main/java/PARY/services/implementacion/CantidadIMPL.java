package PARY.services.implementacion;

import PARY.DAO.ICantidadDAO;
import PARY.entity.pktAct.Cantidad;
import PARY.services.contratos.ICantidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CantidadIMPL extends GenericsImpl<Cantidad, ICantidadDAO> implements ICantidadService {
    @Autowired
    public CantidadIMPL(ICantidadDAO dao) {
        super(dao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cantidad> findCantidadesByCantReserv(int cantReserv) {
        return DAO.findCantidadesByCantReserv(cantReserv);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cantidad> findCantidadesByCantMeEncanta(int cantMeEncanta) {
        return DAO.findCantidadesByCantReserv(cantMeEncanta);
    }
}
