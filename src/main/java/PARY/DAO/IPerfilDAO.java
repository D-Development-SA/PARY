package PARY.DAO;

import PARY.entity.Perfil;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPerfilDAO extends CrudRepository<Perfil, Long> {
    public List<Perfil> findPerfilesByNombreContains(String nombre);
    public List<Perfil> findPerfilesByCiContains(String CI);
}
