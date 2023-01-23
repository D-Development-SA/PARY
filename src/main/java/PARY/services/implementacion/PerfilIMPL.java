package PARY.services.implementacion;

import PARY.DAO.IPerfilDAO;
import PARY.entity.Direccion;
import PARY.entity.Perfil;
import PARY.services.contratos.IPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilIMPL extends GenericsImpl<Perfil, IPerfilDAO> implements IPerfilService {
    @Autowired
    public PerfilIMPL(IPerfilDAO dao) {
        super(dao);
    }

    @Override
    public List<Perfil> findPerfilesByNombreContains(String nombre) {
        return DAO.findPerfilesByNombreContains(nombre);
    }

    @Override
    public List<Perfil> findPerfilesByCiContains(String CI) {
        return DAO.findPerfilesByCiContains(CI);
    }
}
