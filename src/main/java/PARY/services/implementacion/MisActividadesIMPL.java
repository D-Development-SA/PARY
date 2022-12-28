package PARY.services.implementacion;

import PARY.DAO.IMisActividadesDAO;
import PARY.entity.MisActividades;
import PARY.services.contratos.IMisActividadesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MisActividadesIMPL extends GenericsImpl<MisActividades, IMisActividadesDAO> implements IMisActividadesServices {
    @Autowired
    public MisActividadesIMPL(IMisActividadesDAO dao) {
        super(dao);
    }
}
