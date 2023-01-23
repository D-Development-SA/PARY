package PARY.hilos;

import PARY.entity.Actividad;
import PARY.entity.Perfil;
import PARY.entity.pktNotifi_Reg.notifiProp;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.NotificacionIMPL;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotifupdateAct implements Runnable {
    private Actividad auxAct;
    private Actividad actividad;
    private List<Perfil> perfils;
    private long id;
    private IPerfilService perfilService;

    @Override
    public void run() {
        notifi();
    }

    private synchronized void notifi(){
        elimPerfilxDireccActaux();

        System.out.println("--------COmenzo hilo de Actualizar ACtividad--------");

        //Aumento de reservaciones
        if(auxAct.getCantidad().getCantReserv() < actividad.getCantidad().getCantReserv() &&
                actividad.getDireccion().getProvincia().equals(auxAct.getDireccion().getProvincia()) &&
                actividad.getDireccion().getMunicipio().equals(auxAct.getDireccion().getMunicipio())
        ){

            //--------------NOTIFICACION----------------------
            NotificacionIMPL.crearNotifiGlobal(notifiProp.notifi[notifiProp.CAPAC_AUMENT],
                    notifiProp.crearInfo(auxAct.getNombre(), notifiProp.CAPAC_AUMENT),
                    perfils, auxAct, id);
        }

        //Cambio de direccion
        if(!auxAct.getDireccion().getMunicipio().equals(actividad.getDireccion().getMunicipio()) &&
                auxAct.getDireccion().getProvincia().equals(actividad.getDireccion().getProvincia())){

            //--------------NOTIFICACION----------------------
            NotificacionIMPL.crearNotifiGlobal(notifiProp.notifi[notifiProp.ACT_UPDATE],
                    notifiProp.crearInfo(auxAct.getNombre(), notifiProp.ACT_UPDATE),
                    perfils, auxAct, id);
        }

        elimPerfilxDireccAct();
        //--------------NOTIFICACION----------------------
        NotificacionIMPL.crearNotifiGlobal(notifiProp.notifi[notifiProp.ACT_UPDATE],
                notifiProp.crearInfo(actividad.getNombre(), notifiProp.ACT_UPDATE),
                perfils, actividad, id);

        System.out.println("--------Termino hilo de Actualizar ACtividad--------");
    }

    private synchronized void elimPerfilxDireccActaux() {
        perfils = perfilService.findAll();
        perfils.removeIf(perfil -> !perfil.getDireccion().getProvincia().equals(auxAct.getDireccion().getProvincia()) &&
                !perfil.getDireccion().getMunicipio().equals(auxAct.getDireccion().getMunicipio())
        );
    }

    private synchronized void elimPerfilxDireccAct() {
        perfils = perfilService.findAll();
        perfils.removeIf(perfil -> !perfil.getDireccion().getProvincia().equals(actividad.getDireccion().getProvincia()) &&
                !perfil.getDireccion().getMunicipio().equals(actividad.getDireccion().getMunicipio())
        );
    }

    public Actividad getAuxAct() {
        return auxAct;
    }

    public void setAuxAct(Actividad auxAct) {
        this.auxAct = auxAct;
    }

    public List<Perfil> getPerfils() {
        return perfils;
    }

    public void setPerfils(List<Perfil> perfils) {
        this.perfils = perfils;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public IPerfilService getPerfilService() {
        return perfilService;
    }

    public void setPerfilService(IPerfilService perfilService) {
        this.perfilService = perfilService;
    }
}
