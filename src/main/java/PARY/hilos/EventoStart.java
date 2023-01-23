package PARY.hilos;

import PARY.controller.ControladorAct;
import PARY.entity.Actividad;
import PARY.entity.pktNotifi_Reg.notifiProp;
import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.NotificacionIMPL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventoStart implements Runnable {
    private final IPerfilService perfilService;
    private final IActividadesService actService;

    private int cont = 0;

    public EventoStart(IPerfilService perfilService, IActividadesService actService) {
        this.perfilService = perfilService;
        this.actService = actService;
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");

        while (true) {
            String FH = formatter.format(LocalDateTime.now());

            System.out.println("--------Comenzo hilo de Comienzo de Eventos--------");

            if (EventCadaHora.fechaH.containsValue(FH)) {

                EventCadaHora.fechaH.forEach((k, v) -> {
                    if (v.equals(FH)) {
                        long id = NotificacionIMPL.crearNotifi();
                        Actividad actividad = actService.findById(k);

                        perfilService.findAll().stream()
                                .filter(perfil -> perfil.getDireccion().getProvincia().equals(actividad.getDireccion().getProvincia()) &&
                                        perfil.getDireccion().getMunicipio().equals(actividad.getDireccion().getMunicipio()))
                                .forEach(perfil -> NotificacionIMPL.crearNotifiPersonal(notifiProp.notifi[notifiProp.EVENT_START],
                                        notifiProp.crearInfo(actividad.getNombre(), notifiProp.EVENT_START),
                                        perfil, actividad, id));
                    }
                });
            }
            System.out.println("--------Pausado hilo Comienzo de Evento--------");
            try {Thread.sleep(60000);} catch (InterruptedException e) {e.printStackTrace();}

            cont++;
            if(cont==50) break;
        }
            System.out.println("--------Termino hilo Comienzo de Evento--------");
    }
}
