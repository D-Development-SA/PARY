package PARY.hilos;

import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.ICantidadService;
import PARY.services.contratos.IPerfilService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public abstract class HiloCentral implements IHiloCentral{
    protected static boolean stopAll = true;
    protected final ICantidadService cantidadService;
    protected final IActividadesService actService;
    protected final IPerfilService perfilService;
    protected static HashMap<Long, String> fechaH = new HashMap<>();
    protected boolean detenerHilo = true;

    public HiloCentral(ICantidadService cantidadService, IActividadesService actService, IPerfilService perfilService) {
        this.cantidadService = cantidadService;
        this.actService = actService;
        this.perfilService = perfilService;
    }

    @Override
    public void setDetenerHilo() {
        detenerHilo = false;
    }

    public static void StopAll(){
        stopAll = false;
    }

    @Override
    public void run() {

    }
}
