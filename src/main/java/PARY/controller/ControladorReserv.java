package PARY.controller;

import PARY.entity.Reservacion;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.entity.pktNotifi_Reg.RegProp;
import PARY.services.contratos.IReservacionService;
import PARY.services.implementacion.RegistroAccionIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControladorReserv {
    @Autowired
    private IReservacionService reserv;

//-----------------------GetMappings-----------------------------------

    /* Devuelve todas las Reservaciones */
    @GetMapping("/reservaciones")
    @ResponseStatus(HttpStatus.OK)
    public List<Reservacion> findAllReserv(){
        return reserv.findAll();
    }

    /* Devuelve la cantidad de Reservaciones */
    @GetMapping("/reservaciones/cantidad")
    @ResponseStatus(HttpStatus.OK)
    public int cantReserv(){
        return reserv.findAll().size();
    }

//-----------------------DeleteMappings-----------------------------------

    /* Elimina una Reservacion en especifico */
    @DeleteMapping("/reservaciones/deleteReserv/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReserv(@PathVariable long id){
        RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, id,
                Constant_RegAcciones.ELIMINACION.name(),
                reserv.findById(id).getActividad().getNombre());

        reserv.deleteById(id);
    }

    /* Elimina todas las Reservaciones que se especifiquen */
    @DeleteMapping("/reservaciones/deleteReserv")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroupReserv(@RequestBody List<Reservacion> reser){
        reser.stream().forEach(a->{
            RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, a.getId(),
                    Constant_RegAcciones.ELIMINACION.name(), a.getActividad().getNombre());
        });


        reserv.deleteAll(reser);
    }

}
