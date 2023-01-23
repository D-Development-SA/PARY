package PARY.services.contratos;

import PARY.entity.Direccion;
import PARY.entity.Perfil;

import java.util.List;

public interface IPerfilService extends IGenericsService<Perfil>{
    List<Perfil> findPerfilesByNombreContains(String nombre);
    List<Perfil> findPerfilesByCiContains(String CI);
}
