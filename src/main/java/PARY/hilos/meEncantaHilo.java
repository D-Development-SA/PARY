package PARY.hilos;

import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.ICantidadService;
import PARY.services.contratos.IPerfilService;
import org.springframework.stereotype.Component;

@Component
public class meEncantaHilo extends HiloCentral{
    public meEncantaHilo(ICantidadService cantidadService, IActividadesService actService, IPerfilService perfilService) {
        super(cantidadService, actService, perfilService);
    }

    @Override
    public void run(){

    }
}
