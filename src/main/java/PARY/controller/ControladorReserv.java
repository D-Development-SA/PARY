package PARY.controller;

import PARY.entity.Reservacion;
import PARY.entity.constantes.Constant_RegAcciones;
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

//-----------------------PutMappings-----------------------------------

    /* Actualiza una Reservacion en especifico */
    @PutMapping("/reservaciones/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Reservacion updateReserv(@PathVariable long id, @RequestBody Reservacion reser){
        Reservacion auxReser = reserv.findById(id);

        auxReser.setAprobacion(reser.isAprobacion());
        auxReser.setEstado(reser.getEstado());

        if(auxReser.isAprobacion() && auxReser.getEstado().equals(Constant_RegAcciones.CAMBIO_ESTADO_N)){
            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, id,
                    Constant_RegAcciones.APROBACION,
                    auxReser.getActividad().getNombre());
            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, id,
                    Constant_RegAcciones.CAMBIO_ESTADO_N,
                    auxReser.getActividad().getNombre());
        }else if (auxReser.isAprobacion() && auxReser.getEstado().equals(Constant_RegAcciones.CAMBIO_ESTADO_P)){
            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, id,
                    Constant_RegAcciones.APROBACION,
                    auxReser.getActividad().getNombre());
            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, id,
                    Constant_RegAcciones.CAMBIO_ESTADO_P,
                    auxReser.getActividad().getNombre());
        }else{
            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, id,
                    Constant_RegAcciones.CANCELACION,
                    auxReser.getActividad().getNombre());
        }


        return reserv.save(auxReser);
    }

//-----------------------DeleteMappings-----------------------------------

    /* Elimina una Reservacion en especifico */
    @DeleteMapping("/reservaciones/deleteReserv/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReserv(@PathVariable long id){
        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, id,
                Constant_RegAcciones.ELIMINACION,
                reserv.findById(id).getActividad().getNombre());

        reserv.deleteById(id);
    }

    /* Elimina todas las Reservaciones que se especifiquen */
    @DeleteMapping("/reservaciones/deleteReserv")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroupReserv(@RequestBody List<Reservacion> reser){
        reser.stream().forEach(a->{
            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, a.getId(),
                    Constant_RegAcciones.ELIMINACION, a.getActividad().getNombre());
        });
        reserv.deleteAll(reser);
    }

}
