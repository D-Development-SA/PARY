package PARY.controller;

import PARY.entity.*;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.entity.constantes.Constant_Reservaciones;
import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.RegistroAccionIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControladorAct {
    @Autowired
    private IActividadesService act;
    @Autowired
    private IPerfilService perfilService;

//-----------------------GetMappings-----------------------------------

    /* Devuelve todas las Actividades */
    @GetMapping("/actividades")
    @ResponseStatus(HttpStatus.OK)
    public List<Actividad> findAllAct(){
        return act.findAll();
    }

    /* Devuelve una Actividad en especifico */
    @GetMapping("/actividades/buscarID/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Actividad findActById(@PathVariable Long id){
        return act.findById(id);
    }

    /* Devuelve todas las Actividades que contengan un nombre en especifico */
    @GetMapping("/actividades/buscarNombres/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public List<Actividad> findByName (@PathVariable String nombre){
        return act.findActividadesByNombreContains(nombre);
    }

    /* Devuelve todas las Actividades ordenadas por fecha */
    @GetMapping("/actividades/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<Actividad> showRecentAct(){
        return act.showRecentAct();
    }

//-----------------------PostMappings-----------------------------------

    /* Crea y devuelve una Actividad */
    @PostMapping("/actividades/crearAct")
    @ResponseStatus(HttpStatus.CREATED)
    public Actividad createAct(@RequestBody Actividad Actividad){
        Actividad aux = act.save(Actividad);

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, aux.getId(),
                                    Constant_RegAcciones.CREACION, aux.getNombre());

        return aux;
    }

//-----------------------PutMappings-----------------------------------

    /* Actualiza una Actividad en especifico y crea una reservacion si el param reser = true */
    @PutMapping("/actividades/actualizarAct/{id}+{reserv}+{idP}")
    @ResponseStatus(HttpStatus.CREATED)
    public Actividad updateAct(@RequestBody Actividad Actividad, @PathVariable long id,
                               @PathVariable boolean reserv, @PathVariable long idP){
        Actividad auxAct = act.findById(id);
        String nombre = auxAct.getNombre();

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, id,
                                    Constant_RegAcciones.ACTUALIZACION,
                             nombre+"->"+ Actividad.getNombre());

        if(reserv){
            Reservacion r = new Reservacion();
            r.setEstado(Constant_Reservaciones.ESTADO_PENDIENTE);
            r.setAprobacion(false);
            r.setActividades(auxAct);
            r.setFechaHora(auxAct.getFechaHora());
            r.getPerfil().add(perfilService.findById(idP));
            auxAct.getReservacion().add(r);
            auxAct.getCantidades().setCantReserv(auxAct.getCantidades().getCantReserv()-1);
        }else {
            auxAct.setReservacion(Actividad.getReservacion());
            auxAct.setCantidades(Actividad.getCantidades());
            auxAct.setNombre(Actividad.getNombre());
            auxAct.setPrecio(Actividad.getPrecio());
            auxAct.setDireccion(Actividad.getDireccion());
            auxAct.setInfo(Actividad.getInfo());
            auxAct.setFechaHora(Actividad.getFechaHora());
            auxAct.setTipoAct(Actividad.getTipoAct());
        }

        Actividad aux = act.save(auxAct);

        if (reserv) RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION,
                aux.getReservacion().get(aux.getReservacion().size()-1).getId(),
                Constant_RegAcciones.RESERVACION, Actividad.getNombre());

        return aux;
    }

//-----------------------DeleteMappings-----------------------------------

    /* Elimina una Actividad en especifico */
    @DeleteMapping("/actividades/deleteAct/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAct(@PathVariable Long id){
        Actividad aux = findActById(id);

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, id,
                Constant_RegAcciones.ELIMINACION, aux.getNombre());

        if (aux.getReservacion() != null) {
            for (Reservacion r :
                    aux.getReservacion()) {
                RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION, r.getId(),
                        Constant_RegAcciones.ELIMINACION, aux.getNombre());
            }
        }
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
        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, -1,
                Constant_RegAcciones.ELIMINACION, "deleteAll");
        act.deleteAll();
    }
}