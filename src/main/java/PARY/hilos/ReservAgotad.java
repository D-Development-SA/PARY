package PARY.hilos;

import PARY.entity.Actividad;
import PARY.entity.Perfil;
import PARY.entity.pktAct.Cantidad;
import PARY.entity.pktNotifi_Reg.notifiProp;
import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.ICantidadService;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.NotificacionIMPL;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservAgotad extends HiloCentral{
    public ReservAgotad(ICantidadService cantidadService, IActividadesService actService, IPerfilService perfilService) {
        super(cantidadService, actService, perfilService);
    }

    @Override
    public void run() {
        List<Cantidad> cantidadList;
        List<Actividad> actividadList;
        List<Perfil> perfilList;

        while (this.detenerHilo && stopAll) {
            System.out.println("--------Comenzo hilo de Reservaciones agotadas--------");

                cantidadList = cantidadService.findCantidadesByCantReserv(0);
                if (!cantidadList.isEmpty()) {
                    actividadList = actService.findAll();
                    perfilList = perfilService.findAll();

                    //temporal
                    List<Actividad> finalActividadList = actividadList;
                    List<Perfil> finalPerfilList = perfilList;
                    //-------------------------------------

                    cantidadList.forEach(cantidad -> {
                        finalActividadList.stream().parallel()
                                .filter(actividad -> actividad.getCantidad().getId().equals(cantidad.getId()))

                                .forEach(actividad -> {
                                    System.out.println("5555555555555555"+"entro");
                                    finalPerfilList.stream()
                                            .filter(perfil -> perfil.getDireccion().getProvincia().equals(actividad.getDireccion().getProvincia()) &&
                                                    perfil.getDireccion().getMunicipio().equals(actividad.getDireccion().getMunicipio()))
                                            .forEach(perfil -> {
                                                long id = NotificacionIMPL.crearNotifi();
                                                NotificacionIMPL.crearNotifiPersonal(notifiProp.notifi[notifiProp.CAPAC_AGOT],
                                                        notifiProp.crearInfo(actividad.getNombre(), notifiProp.CAPAC_AGOT),
                                                        perfil, actividad,id);
                                            });
                                });
                    });
                    System.out.println("--------Notificado con exito ReservAgot--------");
                }

            System.out.println("--------Pausado hilo de Reservaciones agotadas--------");

            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Termino hilo de Reservaciones agotadas");
    }
}
