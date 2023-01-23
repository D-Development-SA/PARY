package PARY.hilos;

import PARY.entity.Actividad;
import PARY.entity.Perfil;
import PARY.entity.pktNotifi_Reg.notifiProp;
import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.NotificacionIMPL;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class notifCrearAct implements Runnable {
    private Actividad aux;
    private IPerfilService perfilService;
    private IActividadesService act;
    private long id;

    @Override
    public void run() {
        System.out.println("--------Notificador ''Actividades creadas'' desplegado--------");

        List<Perfil> perfils = perfilService.findAll();

        perfils.removeIf(perfil -> !perfil.getDireccion().getProvincia().equals(aux.getDireccion().getProvincia()) &&
                !perfil.getDireccion().getMunicipio().equals(aux.getDireccion().getMunicipio()));

        //--------------NOTIFICACION----------------------
        NotificacionIMPL.crearNotifiGlobal(notifiProp.notifi[notifiProp.ACT_NUEVA_LOCAL],
                notifiProp.crearInfo(aux.getNombre(), notifiProp.ACT_NUEVA_LOCAL),
                perfils, aux, id);

        System.out.println("--------Agregando a BD la notificacion--------");
        perfilService.saveAll(perfils);

        System.out.println("--------Notificador terminado--------");
    }

    public Actividad getAux() {
        return aux;
    }

    public void setAux(Actividad aux) {
        this.aux = aux;
    }

    public IPerfilService getPerfilService() {
        return perfilService;
    }

    public void setPerfilService(IPerfilService perfilService) {
        this.perfilService = perfilService;
    }

    public IActividadesService getAct() {
        return act;
    }

    public void setAct(IActividadesService act) {
        this.act = act;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
