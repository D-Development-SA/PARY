package PARY.controller;

import PARY.entity.*;
import PARY.entity.clasesAux.ComentarioAux;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.entity.constantes.Constant_Reservaciones;
import PARY.entity.pktAct.Comentario;
import PARY.entity.pktAct.Reaccion;
import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.RegistroAccionIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
    public List<Actividad> findAllAct() {
        return act.findAll();
    }

    /* Devuelve una Actividad en especifico */
    @GetMapping("/actividades/buscarID/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Actividad findActById(@PathVariable Long id) {
        return act.findById(id);
    }

    /* Devuelve todas las Actividades que contengan un nombre en especifico */
    @GetMapping("/actividades/buscarNombres/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public List<Actividad> findByName(@PathVariable String nombre) {
        return act.findActividadesByNombreContains(nombre);
    }

    /* Devuelve todas las Actividades ordenadas por fecha */
    @GetMapping("/actividades/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<Actividad> showRecentAct() {
        return act.showRecentAct();
    }

    /* Devuelve la cantidad de comentarios de la actividad */
    @GetMapping("/actividades/comentario/cantComentarios/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int cantComentarios(@PathVariable long id) {
        return act.findById(id).getCantidad().getCantComent();
    }

    /* Devuelve la cantidad de me encanta de la actividad */
    @GetMapping("/actividades/cantMeEncanta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int cantMeEncanta(@PathVariable long id) {
        return act.findById(id).getCantidad().getCantMeEncanta();
    }

    /* Devuelve los comentarios de la actividad */
    @GetMapping("/actividades/comentario/findAllComentarios/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> findAllComentarios(@PathVariable long id) {
        List<String> nombres = new ArrayList<>();
        AtomicInteger i = new AtomicInteger(0);

        return act.findById(id).getCantidad().getReaccions().stream()
                //Agrego los nombres de los perfiles a una lista
                .map(reaccion -> {
                    nombres.add(reaccion.getNombreP());
                    return reaccion.getComentarios();
                })

                .parallel()
                //Convierto las reacciones en una Lista de comentarios
                .flatMap(comentarios1 -> comentarios1.stream())

                //Agrego le agrego a los comentarios los nombres y la fecha
                .map(comentario -> {
                    comentario.setText(nombres.get(i.get()) + comentario.getText() + "\n\n" + comentario.getTextUFechaH());
                    i.getAndIncrement();
                    return comentario;
                })
                //Organizo los comentarios x fecha
                .sorted(Comparator.comparing(Comentario::getFechaH))
                //Transformo en String los Comentarios
                .map(comentario -> comentario.getText())
                .toList();
    }

    /* Devuelve la cantidad de Activides */
    @GetMapping("/actividades/cantidad")
    @ResponseStatus(HttpStatus.OK)
    public int findAllReserv(){
        return act.findAll().size();
    }

//-----------------------PostMappings-----------------------------------

    /* Crea y devuelve una Actividad */
    @PostMapping("/actividades/crearAct")
    @ResponseStatus(HttpStatus.CREATED)
    public Actividad createAct(@RequestBody Actividad Actividad) {
        Actividad aux = act.save(Actividad);

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, aux.getId(),
                Constant_RegAcciones.CREACION, aux.getNombre());

        return aux;
    }

//-----------------------PutMappings-----------------------------------

    /* Actualiza una Actividad en especifico */
    @PutMapping("/actividades/actualizarAct/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Actividad updateAct(@RequestBody Actividad Actividad, @PathVariable long id) {
        Actividad auxAct = act.findById(id);
        String nombre = auxAct.getNombre();

        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, id,
                Constant_RegAcciones.ACTUALIZACION,
                nombre + "->" + Actividad.getNombre());

        auxAct.setReservacion(Actividad.getReservacion());
        auxAct.setCantidad(Actividad.getCantidad());
        auxAct.setNombre(Actividad.getNombre());
        auxAct.setPrecio(Actividad.getPrecio());
        auxAct.setDireccion(Actividad.getDireccion());
        auxAct.setInfo(Actividad.getInfo());
        auxAct.setFechaHora(Actividad.getFechaHora());
        auxAct.setTipoAct(Actividad.getTipoAct());

        return act.save(auxAct);
    }

//-----------------------PatchMappings-----------------------------------

    /* Agrega un comentario del perfil */
    @PatchMapping("/actividades/comentario/AgregarComentario/{id}+{idP}")
    @ResponseStatus(HttpStatus.CREATED)
    public Actividad AgregarComentario(@PathVariable long id, @RequestBody Comentario coment, @PathVariable long idP) {
        Actividad a = act.findById(id);
        Reaccion reaccionAux = null;
        AtomicInteger i = new AtomicInteger(-1);

        if (a.getCantidad().getReaccions().isEmpty()) {
            reaccionAux = addReaccionAAct(idP, new Reaccion(), true);
            reaccionAux.getComentarios().add(coment);
        } else {
            reaccionAux = a.getCantidad().getReaccions().stream()
                    .parallel()
                    .filter(reaccion -> reaccion.getIdPerfil() == idP)
                    .map(reaccion -> {
                        i.set(a.getCantidad().getReaccions().indexOf(reaccion));
                        reaccion.getComentarios().add(coment);
                        return reaccion;
                    })
                    .findFirst()
                    .orElseGet(() -> {
                        Reaccion reac = addReaccionAAct(idP, new Reaccion(), true);
                        reac.getComentarios().add(coment);
                        return reac;
                    });
        }

        if (i.get() > -1)
            a.getCantidad().getReaccions().set(i.get(), reaccionAux);
        else a.getCantidad().getReaccions().add(reaccionAux);

        a.getCantidad().setCantComent(a.getCantidad().getCantComent() + 1);

        System.out.println(a.getCantidad().getReaccions().size());
        //No pueden existir comentarios duplicados
        return act.save(a);
    }

    /* Actualiza los comentarios de una actividad */
    @PatchMapping("/actividades/comentario//CambiarComentario/{id}+{idP}")
    @ResponseStatus(HttpStatus.CREATED)
    public Actividad actualizarComentario(@PathVariable long id, @RequestBody ComentarioAux comentarioAux, @PathVariable long idP) {
        Actividad aux = act.findById(id);
        Reaccion reaccion;

        reaccion = aux.getCantidad().getReaccions().stream()
                .parallel()
                .filter(reacc -> reacc.getIdPerfil().equals(idP))
                .findFirst()
                .map(reacc -> {
                    AtomicInteger i = new AtomicInteger(-1);

                    reacc.getComentarios().forEach(a -> {
                        if (a.getText().equals(comentarioAux.getTextViejo()) && i.get() == -1) {
                            i.set(reacc.getComentarios().indexOf(a));
                        }
                    });

                    reacc.getComentarios().get(i.get()).setText(comentarioAux.getTextNuevo());
                    return reacc;
                })
                .orElseGet(() -> null);

        if (reaccion.equals(null))
            return aux;
        else {
            for (int i = 0; i < aux.getCantidad().getReaccions().size(); i++) {
                if (reaccion.getIdPerfil().equals(aux.getCantidad().getReaccions().get(i).getIdPerfil())) {
                    aux.getCantidad().getReaccions().set(i, reaccion);
                    break;
                }
            }
        }

        return act.save(aux);
    }

    /* Annadir un me encanta de un perfil */
    //id: id de la actividad, idP: id del perfil, addOrDel: valor del boton si esta activado o no
    @PatchMapping("/actividades/addOrDelMeEncanta/{id}+{add}+{idP}")
    @ResponseStatus(HttpStatus.OK)
    public void addOrDelMeEncanta(@PathVariable long id, @PathVariable boolean add, @PathVariable long idP) {
        Actividad actividad = act.findById(id);
        AtomicBoolean val = new AtomicBoolean(true);

        actividad.getCantidad().getReaccions().forEach(reaccion -> {
            if (reaccion.getIdPerfil().equals(idP)) {
                val.set(false);
                valMeEncanta(add, actividad, reaccion);
            }
        });
        if (val.get()) {
            Reaccion reaccion = addReaccionAAct(idP, new Reaccion(), true);
            actividad.getCantidad().getReaccions().add(reaccion);
            valMeEncanta(add, actividad, reaccion);
        }
        act.save(actividad);
    }

    /* Agrega o elimina la actividad de MisActividades */
    @PatchMapping("/actividades/addActMisActiv/{id}+{add}+{idP}")
    @ResponseStatus(HttpStatus.OK)
    public Perfil addActMisActiv(@PathVariable long id, @PathVariable boolean add, @PathVariable long idP) {
        Actividad actividad = act.findById(id);
        Perfil perfil = perfilService.findById(idP);
        Reaccion reac = new Reaccion();
        AtomicBoolean val = new AtomicBoolean(true);

        actividad.getCantidad().getReaccions().forEach(reaccion -> {
            if (reaccion.getIdPerfil() == idP) {
                val.set(false);
                if (reaccion.isValAddMisActividades() && !add) {
                    perfil.getMisActividades()
                            .removeIf(misActividades1 -> misActividades1.equals(actividad));
                    reaccion.setValAddMisActividades(add);
                } else if (!reaccion.isValAddMisActividades() && add) {
                    reaccion.setValAddMisActividades(add);
                    perfil.getMisActividades().add(actividad);
                }
            }
        });

        if (val.get() && add) {
            reac.setIdPerfil(perfil.getId());
            reac.setNombreP(perfil.getNombre());
            reac.setApellidoP(perfil.getApellidos());
            reac.setValAddMisActividades(add);
            actividad.getCantidad().getReaccions().add(reac);
            perfil.getMisActividades().add(actividad);
        }

        act.save(actividad);
        return perfilService.save(perfil);
    }

    /* Crea una reservacion si el param reser = true */
    @PatchMapping("/actividades/creaReser/{id}+{reserv}+{idP}")
    @ResponseStatus(HttpStatus.CREATED)
    public Perfil crearReser(@PathVariable long id,
                                @PathVariable boolean reserv, @PathVariable long idP) {
        Actividad auxAct = act.findById(id);
        AtomicBoolean val = new AtomicBoolean(true);
        Perfil perfil = perfilService.findById(idP);

        System.out.println("comienza");
        System.out.println(auxAct.getCantidad().getReaccions().size());
        //Crea o quita la reservacion si el perfil ya reacciono
        auxAct.getCantidad().getReaccions().stream().forEach(reaccion -> {
            System.out.println("entro al foreach");
            if (reaccion.getIdPerfil() == idP) {
                System.out.println("encontro el perfil");
                val.set(false);
                if (reaccion.isValReser() && !reserv) {
                    System.out.println("entro al primer if");
                    perfil.getReservacion().removeIf(reservacion -> {
                        if(reservacion.getActividad().equals(auxAct)){
                            reservacion.getPerfil().removeIf(perfil1 -> perfil1.equals(perfil));
                            return true;
                        };
                        return false;
                    });
                    auxAct.getCantidad().setCantReserv(auxAct.getCantidad().getCantReserv() - 1);
                    reaccion.setValReser(reserv);
                    System.out.println("salio al primer if");
                } else if (!reaccion.isValReser() && reserv) {
                    System.out.println("entro al segundo if");
                    addReser(reserv, auxAct, perfil, reaccion);
                    auxAct.getCantidad().setCantReserv(auxAct.getCantidad().getCantReserv() + 1);
                    System.out.println("salio al segundo if");
                }
            }
        });

        //Si no ha reaccionado crea la reaccion y reserva
        if (val.get()) {
            System.out.println("entro al tercer if");
            Reaccion reaccion = addReaccionAAct(perfil);
            auxAct.getCantidad().getReaccions().add(reaccion);
            addReser(reserv, auxAct, perfil, reaccion);
            if (reserv) auxAct.getCantidad().setCantReserv(auxAct.getCantidad().getCantReserv() + 1);
            System.out.println("salio al tercer if");
        }

        Actividad aux = act.save(auxAct);
        Perfil perfAux = perfilService.save(perfil);

        if (reserv) RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION,
                aux.getReservacion().get(aux.getReservacion().size() - 1).getId(),
                Constant_RegAcciones.RESERVACION, aux.getNombre());

        System.out.println("se acabo");
        return perfAux;
    }

//-----------------------DeleteMappings-----------------------------------

    /* Elimina una Actividad en especifico */
    @DeleteMapping("/actividades/deleteAct/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAct(@PathVariable Long id) {
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
    public void deleteOldAct() {
        act.deleteOldAct();
    }

    /* Elimina todas las Actividades */
    @DeleteMapping("/actividades/deleteAllAct")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllAct() {
        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD, -1,
                Constant_RegAcciones.ELIMINACION, "deleteAll");
        act.deleteAll();
    }

//________________________________Metodos Auxiliares________________________________________________________________
    private void valMeEncanta(boolean add, Actividad actividad, Reaccion reaccion) {
        if (reaccion.isValMeEncanta() && !add) {
            actividad.getCantidad().setCantMeEncanta(actividad.getCantidad().getCantMeEncanta() - 1);
            reaccion.setValMeEncanta(add);
        } else if (!reaccion.isValMeEncanta() && add) {
            actividad.getCantidad().setCantMeEncanta(actividad.getCantidad().getCantMeEncanta() + 1);
            reaccion.setValMeEncanta(add);
        }
    }

    private Reaccion addReaccionAAct(long idP, Reaccion re, boolean v) {
        System.out.println("entro al metodo");
        Perfil perfil = perfilService.findById(idP);
        if (v) {
            Reaccion reac = re;
            reac.setIdPerfil(perfil.getId());
            reac.setNombreP(perfil.getNombre());
            reac.setApellidoP(perfil.getApellidos());
            reac.setComentarios(new ArrayList<>());
            return reac;
        } else {
            re.setIdPerfil(perfil.getId());
            re.setNombreP(perfil.getNombre());
            re.setApellidoP(perfil.getApellidos());
            return re;
        }
    }

    private Reaccion addReaccionAAct(Perfil perfil) {
        Reaccion reac = new Reaccion();
        reac.setIdPerfil(perfil.getId());
        reac.setNombreP(perfil.getNombre());
        reac.setApellidoP(perfil.getApellidos());
        return reac;
    }

    private void addReser(boolean reserv, Actividad auxAct, Perfil perfil, Reaccion reaccion) {
        reaccion.setValReser(reserv);
        Reservacion r = new Reservacion();
        r.setEstado(Constant_Reservaciones.ESTADO_PENDIENTE);
        r.setAprobacion(false);
        r.setActividad(auxAct);
        r.setFechaHora(auxAct.getFechaHora());
        r.setPerfil(new HashSet<>());
        r.getPerfil().add(perfil);
        perfil.getReservacion().add(r);
    }
}