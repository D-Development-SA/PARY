package PARY.controller;

import PARY.entity.*;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.entity.constantes.Constant_Reservaciones;
import PARY.services.contratos.IMisActividadesService;
import PARY.services.implementacion.RegistroAccionIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControladorAct {
    @Autowired
    private IMisActividadesService act;

//-----------------------GetMappings-----------------------------------

    /* Devuelve todas las Actividades */
    @GetMapping("/actividades")
    @ResponseStatus(HttpStatus.OK)
    public List<MisActividades> findAllAct(){
        return act.findAll();
    }

    /* Devuelve una Actividad en especifico */
    @GetMapping("/actividades/buscarID/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MisActividades findActById(@PathVariable Long id){
        return act.findById(id);
    }

    /* Devuelve todas las Actividades que contengan un nombre en especifico */
    @GetMapping("/actividades/buscarNombres/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public List<MisActividades> findByName (@PathVariable String nombre){
        return act.findActividadesByNombreContains(nombre);
    }

    /* Devuelve todas las Actividades ordenadas por fecha */
    @GetMapping("/actividades/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<MisActividades> showRecentAct(){
        return act.showRecentAct();
    }

//-----------------------PostMappings-----------------------------------

    /* Crea y devuelve una Actividad */
    @PostMapping("/actividades/crearAct")
    @ResponseStatus(HttpStatus.CREATED)
    public MisActividades createAct(@RequestBody MisActividades misActividades){
        MisActividades aux = act.save(misActividades);

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, aux.getId(),
                                    Constant_RegAcciones.CREACION, aux.getNombre());

        return aux;
    }

//-----------------------PutMappings-----------------------------------

    /* Actualiza una Actividad en especifico */
    @PutMapping("/actividades/actualizarAct/{id}+{reserv}")
    @ResponseStatus(HttpStatus.CREATED)
    public MisActividades updateAct(@RequestBody MisActividades misActividades, @PathVariable Long id, @PathVariable boolean reserv){
        MisActividades auxAct = act.findById(id);
        String nombre = auxAct.getNombre();

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, id,
                                    Constant_RegAcciones.ACTUALIZACION,
                             nombre+"->"+misActividades.getNombre());

        auxAct.setNombre(misActividades.getNombre());
        auxAct.setPrecio(misActividades.getPrecio());
        auxAct.setDireccion(misActividades.getDireccion());
        auxAct.setCoordenadas(misActividades.getCoordenadas());
        auxAct.setInfo(misActividades.getInfo());
        auxAct.setFechaHora(misActividades.getFechaHora());
        auxAct.setTipoAct(misActividades.getTipoAct());
        if(reserv){
            Reservacion r = new Reservacion();
            r.setEstado(Constant_Reservaciones.ESTADO_PENDIENTE);
            r.setAprobacion(false);
            r.setActividades(auxAct);
            auxAct.setReservacion(r);
            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, id,
                    Constant_RegAcciones.RESERVACION,
                    nombre+"->"+misActividades.getNombre());
        }else auxAct.setReservacion(misActividades.getReservacion());
        auxAct.setPerfil(misActividades.getPerfil());


        return act.save(auxAct);
    }

//-----------------------DeleteMappings-----------------------------------

    /* Elimina una Actividad en especifico */
    @DeleteMapping("/actividades/deleteAct/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAct(@PathVariable Long id){
        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, id, Constant_RegAcciones.ELIMINACION, findActById(id).getNombre());
        act.deleteById(id);
    }

    /* Elimina todas las Actividades que ya hayan transcurrido */
    @DeleteMapping("/actividades/deleteOldAct")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOldAct(){
        act.deleteOldAct();
    }

    /* Elimina todas las Actividades */
    @DeleteMapping("/actividades/deleteAllAct")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllAct(){
        act.deleteAll();
    }
}