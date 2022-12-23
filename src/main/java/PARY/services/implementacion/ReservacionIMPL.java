package PARY.services.implementacion;

import PARY.DAO.IReservacionDAO;
import PARY.entity.Reservacion;
import PARY.services.contratos.IReservacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservacionIMPL extends GenericsImpl<Reservacion, IReservacionDAO> implements IReservacionService {
    @Autowired
    public ReservacionIMPL(IReservacionDAO dao) {
        super(dao);
    }

    @Override
    @Transactional
    public void deleteAll(List<Reservacion> reser){
        DAO.deleteAll(reser);
    }
}
