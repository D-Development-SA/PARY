package PARY.controller;

import PARY.entity.Actividad;
import PARY.entity.Perfil;
import PARY.entity.Reservacion;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.RegistroAccionIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ControladorPerfil {
    @Autowired
    private IPerfilService perfil;

//-----------------------GetMappings-----------------------------------

    /* Devuelve todos los Perfiles */
    @GetMapping("/perfiles")
    @ResponseStatus(HttpStatus.OK)
    public List<Perfil> findAllPerfil(){
        return perfil.findAll();
    }

    /* Devuelve un Perfil en especifico */
    @GetMapping("/perfiles/buscarID/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Perfil findPerfilById(@PathVariable Long id){
        return perfil.findById(id);
    }

    /* Devuelve todas las Perfil que contengan un nombre en especifico */
    @GetMapping("/perfiles/buscarNombres/{nombres}")
    @ResponseStatus(HttpStatus.OK)
    public List<Perfil> findByName (@PathVariable String nombres){
        return perfil.findPerfilesByNombreContains(nombres);
    }

    /* Devuelve todos los Perfiles que contengan un fragmento del CI especificado */
    @GetMapping("/perfiles/buscarCI/{CI}")
    @ResponseStatus(HttpStatus.OK)
    public List<Perfil> findByCI(@PathVariable String CI){
        return perfil.findPerfilesByCiContains(CI);
    }

    /* Devuelve todas las actividad add a mis actividades de un perfil */
    @GetMapping("/perfiles/findAllMisAct/{idP}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Actividad> findAllMisAct(@PathVariable long idP) {
        return perfil.findById(idP).getMisActividades();
    }

    /* Devuelve todas las reservaciones de un perfil */
    @GetMapping("/perfiles/findAllReservPerf/{idP}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Reservacion> findAllReservPerf(@PathVariable long idP) {
        return perfil.findById(idP).getReservacion();
    }

    /* Devuelve la cantidad de reservaciones de un perfil */
    @GetMapping("/perfiles/cantReservPerf/{idP}")
    @ResponseStatus(HttpStatus.OK)
    public int cantReservPerf(@PathVariable long idP){
        return perfil.findById(idP).getReservacion().size();
    }

    /* Devuelve cantidad de perfiles */
    @GetMapping("/perfiles/cantidad")
    @ResponseStatus(HttpStatus.OK)
    public int cantPerf(){
        return perfil.findAll().size();
    }

//-----------------------PostMappings-----------------------------------

    /* Crea y devuelve un Perfil */
    @PostMapping("perfiles/crearPerfil")
    @ResponseStatus(HttpStatus.CREATED)
    public Perfil createPerfil(@RequestBody Perfil perfill){
        Perfil aux = perfil.save(perfill);

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_PERFIL, aux.getId(),
                Constant_RegAcciones.CREACION, aux.getNombre());

        return aux;
    }

//-----------------------PutMappings-----------------------------------

    /* Actualiza un Perfil en especifico */
    @PutMapping("/perfiles/actualizarPerfil/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Perfil updatePerfil(@RequestBody Perfil perfill, @PathVariable long id){
        Perfil auxP = perfil.findById(id);
        String nombre = auxP.getNombre();

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, id,
                Constant_RegAcciones.ACTUALIZACION,
                nombre+"->"+ auxP.getNombre());

        auxP.setReservacion(perfill.getReservacion());
        auxP.setNombre(perfill.getNombre());
        auxP.setContrasenna(perfill.getContrasenna());
        auxP.setCi(perfill.getCi());
        auxP.setApellidos(perfill.getApellidos());
        auxP.setMisActividades(perfill.getMisActividades());

        return perfil.save(auxP);
    }

//-----------------------PatchMappings-----------------------------------
    /* Agrega las reservaciones a mis actividades de un Perfil en especifico */
    @PatchMapping("/perfiles/addReservAMisAct")
    @ResponseStatus(HttpStatus.CREATED)
    public Perfil addReservAMisAct(@PathVariable long id){
        Perfil perfil = findPerfilById(id);
        if (!perfil.getReservacion().isEmpty())
            perfil.getReservacion().forEach(reservacion -> perfil.getMisActividades().add(reservacion.getActividad()));
        return perfil;
    }
//-----------------------DeleteMappings-----------------------------------

    /* Elimina un Perfil en especifico */
    @DeleteMapping("/perfiles/deletePerfil/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerfil(@PathVariable Long id){
        Perfil aux = perfil.findById(id);

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, id,
                Constant_RegAcciones.ELIMINACION, aux.getNombre());

        perfil.deleteById(id);
    }

    /* Elimina todas los Perfiles */
    @DeleteMapping("/perfiles/deleteAllAct")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllPerfil(){
        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, -1,
                Constant_RegAcciones.ELIMINACION, "deleteAll");
        perfil.deleteAll();
    }

}
