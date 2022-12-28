package PARY.services.implementacion;

import PARY.DAO.IReaccionDAO;
import PARY.entity.pktAct.Reaccion;
import PARY.services.contratos.IReaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReaccionIMPL extends GenericsImpl<Reaccion, IReaccionDAO> implements IReaccionService {
    @Autowired
    public ReaccionIMPL(IReaccionDAO dao) {
        super(dao);
    }
}
