package PARY;

import PARY.entity.Reservacion;
import PARY.hilos.EventCadaHora;
import PARY.services.contratos.IReservacionService;
import PARY.services.implementacion.ReservacionIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectParyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectParyApplication.class, args);
    }
}
