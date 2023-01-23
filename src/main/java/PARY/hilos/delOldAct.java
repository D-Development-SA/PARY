package PARY.hilos;

import PARY.entity.Actividad;
import PARY.entity.Perfil;
import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.NotificacionIMPL;

import java.util.List;

public class delOldAct extends HiloCentral{
    public delOldAct(IActividadesService actService, IPerfilService perfilService) {
        super(null, actService, perfilService);
    }

    @Override
    public void run() {
        while (this.detenerHilo && stopAll){
            System.out.println("--------Comenzo hilo delOldAct--------");
            List<Actividad> aux;

            if (!(aux = actService.deleteOldAct()).isEmpty()) {
                List<Perfil> Perfilaux = perfilService.findAll();

                aux.forEach(actividad -> {
                    NotificacionIMPL.EliminarNotiAct(actividad)
                            .forEach(idN -> Perfilaux.forEach(perfil -> perfil.getNotificacion()
                                    .removeIf(notificacion -> notificacion.getId().equals(idN))
                            ));
                });

                perfilService.saveAll(Perfilaux);
                actService.deleteAll(aux);
            }
            System.out.println("--------Pausado hilo delOldAct--------");

            try {Thread.sleep(86400000);} catch (InterruptedException e) {e.printStackTrace();}
        }
            System.out.println("--------Termino hilo delOldAct--------");
    }
}
