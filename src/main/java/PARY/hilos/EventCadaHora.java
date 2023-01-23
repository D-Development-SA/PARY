package PARY.hilos;

import PARY.entity.Actividad;
import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.IPerfilService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class EventCadaHora extends HiloCentral{
    Thread thread;

    public EventCadaHora(IActividadesService actService, IPerfilService perfilService) {
        super(null, actService, perfilService);
    }

    @Override
    public void run() {
        thread = new Thread(new EventoStart(perfilService, actService));

        try{
            while (this.detenerHilo && stopAll){
                System.out.println("--------Comenzo hilo de Evento Cada hora--------");

                fechaH.clear();
                List<Actividad> actividads = actService.findAll();
                actividads.stream().parallel().forEach(actividad -> fechaH.put(actividad.getId(), actividad.getFechaHora()));
                if(!thread.isAlive()) thread.start();
                thread.setName("Find_Event_1min");

                System.out.println("--------Pausado hilo de Evento Cada Hora--------");

                try {Thread.sleep(3600000);} catch (InterruptedException e) {e.printStackTrace();}
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("--------Termino hilo de Evento Cada Hora--------");
    }

    public Thread getThread() {
        return thread;
    }
}
