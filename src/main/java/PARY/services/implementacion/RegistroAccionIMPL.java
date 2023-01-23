package PARY.services.implementacion;

import PARY.DAO.IRegistroAccionDAO;
import PARY.entity.RegistroAccion;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.services.contratos.IRegistroAccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistroAccionIMPL extends GenericsImpl<RegistroAccion, IRegistroAccionDAO> implements IRegistroAccionService {
    private static IRegistroAccionDAO regDao;

    @Autowired
    public RegistroAccionIMPL(IRegistroAccionDAO dao) {
        super(dao);
        regDao = super.DAO;
    }

    public static void crearReg(String tipo, long idTipo, String accion, String nombre){
        regDao.save(new RegistroAccion(idTipo,tipo,accion,nombre));
    }

}
