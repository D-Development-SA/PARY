package PARY.controller;

import PARY.entity.*;
import PARY.entity.clasesAux.ComentarioAux;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.entity.constantes.Constant_Reservaciones;
import PARY.entity.pktAct.Comentario;
import PARY.entity.pktAct.Reaccion;
import PARY.entity.pktNotifi_Reg.RegProp;
import PARY.entity.pktNotifi_Reg.notifiProp;
import PARY.hilos.*;
import PARY.services.contratos.IActividadesService;
import PARY.services.contratos.ICantidadService;
import PARY.services.contratos.IPerfilService;
import PARY.services.implementacion.NotificacionIMPL;
import PARY.services.implementacion.RegistroAccionIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class ControladorAct {
    @Autowired
    private IActividadesService act;
    @Autowired
    private IPerfilService perfilService;
    @Autowired
    private ICantidadService cantidadService;

    private HashMap<Thread, IHiloCentral> hashMap = new HashMap<>();
    private String[] nameHilos = {"EventCadaHora","delOldAct","ReservAgotad"};
    private boolean bool = true;

    //-----------------------GetMappings-----------------------------------

    /* Devuelve todas las Actividades */
    @GetMapping("/actividades")
    @ResponseStatus(HttpStatus.OK)
    public List<Actividad> findAllAct() {
        return act.findAll();
    }

    /* Devuelve todas las Actividades de una localidad */
    @GetMapping("/actividades/{provincia}")
    @ResponseStatus(HttpStatus.OK)
    public List<Actividad> findAllActProvincia(@PathVariable String provincia) {
        return act.findActividadesByProvinciaOrdenadas(provincia);
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
        long id = NotificacionIMPL.crearNotifi();
        Actividad aux = act.save(Actividad);
        notifCrearAct notifCrearAct = new notifCrearAct();
        notifCrearAct.setAux(aux);
        notifCrearAct.setPerfilService(perfilService);
        notifCrearAct.setId(id);
        Thread thread = new Thread(notifCrearAct, "Notificador");

        RegistroAccionIMPL.crearReg(RegProp.TIPO_ACTIVIDAD, aux.getId(),
                Constant_RegAcciones.CREACION.name(), aux.getNombre());

        thread.start();

        return aux;
    }

//-----------------------PutMappings-----------------------------------

    /* Actualiza una Actividad en especifico */
    @PutMapping("/actividades/actualizarAct/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Actividad updateAct(@RequestBody Actividad Actividad, @PathVariable long id) {
        long idN = NotificacionIMPL.crearNotifi();

        Actividad auxAct = act.findById(id);
        String nombre = auxAct.getNombre();

        NotifupdateAct notifupdateAct = new NotifupdateAct();
        notifupdateAct.setAuxAct(auxAct);
        notifupdateAct.setPerfils(new ArrayList<>());
        notifupdateAct.setActividad(Actividad);
        notifupdateAct.setId(idN);
        Thread thread = new Thread(notifupdateAct);

        RegistroAccionIMPL.crearReg(RegProp.TIPO_ACTIVIDAD, id,
                Constant_RegAcciones.ACTUALIZACION.name(),
                nombre + "->" + Actividad.getNombre());

        thread.start();

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

        RegistroAccionIMPL.crearReg(RegProp.TIPO_ACTIVIDAD, id,
                Constant_RegAcciones.CREACION.name() + "-Comentario",
                a.getNombre());

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

        RegistroAccionIMPL.crearReg(RegProp.TIPO_ACTIVIDAD, id,
                Constant_RegAcciones.ACTUALIZACION.name() + "-Comentario",
                aux.getNombre());

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

                    RegistroAccionIMPL.crearReg(RegProp.TIPO_PERFIL, idP,
                            Constant_RegAcciones.ELIMINACION.name() + "-MisActividades"+ actividad.getNombre(),
                            perfil.getNombre());

                } else if (!reaccion.isValAddMisActividades() && add) {
                    reaccion.setValAddMisActividades(add);
                    perfil.getMisActividades().add(actividad);

                    RegistroAccionIMPL.crearReg(RegProp.TIPO_PERFIL, idP,
                            Constant_RegAcciones.CREACION.name() + "-MisActividades"+ actividad.getNombre(),
                            perfil.getNombre());

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

            RegistroAccionIMPL.crearReg(RegProp.TIPO_PERFIL, idP,
                    Constant_RegAcciones.CREACION.name() + "-MisActividades"+ actividad.getNombre(),
                    perfil.getNombre());

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

        //Crea o quita la reservacion si el perfil ya reacciono
        auxAct.getCantidad().getReaccions().stream().forEach(reaccion -> {
            if (reaccion.getIdPerfil() == idP) {
                val.set(false);
                if (reaccion.isValReser() && !reserv) {
                    perfil.getReservacion().removeIf(reservacion -> {
                        if(reservacion.getActividad().equals(auxAct)){
                            reservacion.getPerfil().removeIf(perfil1 -> perfil1.equals(perfil));

                            //--------------NOTIFICACION----------------------
                            NotificacionIMPL.crearNotifiPersonal(notifiProp.notifi[notifiProp.RESER_ELIM],
                                    notifiProp.crearInfo(auxAct.getNombre(), notifiProp.RESER_ELIM),
                                    perfil, auxAct, NotificacionIMPL.crearNotifi());

                            RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, idP,
                                    Constant_RegAcciones.ELIMINACION.name() + auxAct.getNombre(),
                                    perfil.getNombre());

                            return true;
                        };
                        return false;
                    });
                    auxAct.getCantidad().setCantReserv(auxAct.getCantidad().getCantReserv() - 1);
                    reaccion.setValReser(reserv);
                } else if (!reaccion.isValReser() && reserv) {
                    addReser(reserv, auxAct, perfil, reaccion);
                    auxAct.getCantidad().setCantReserv(auxAct.getCantidad().getCantReserv() + 1);

                    RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, idP,
                            Constant_RegAcciones.CREACION.name() + auxAct.getNombre(),
                            perfil.getNombre());

                    RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, idP,
                            Constant_RegAcciones.ESTADO_PENDIENTE + auxAct.getNombre(),
                            perfil.getNombre());

                }
            }
        });

        //Si no ha reaccionado crea la reaccion y reserva
        if (val.get()) {
            Reaccion reaccion = addReaccionAAct(perfil);
            auxAct.getCantidad().getReaccions().add(reaccion);
            addReser(reserv, auxAct, perfil, reaccion);
            if (reserv) auxAct.getCantidad().setCantReserv(auxAct.getCantidad().getCantReserv() + 1);

            RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, idP,
                    Constant_RegAcciones.CREACION.name() + auxAct.getNombre(),
                    perfil.getNombre());

        }

        Actividad aux = act.save(auxAct);
        Perfil perfAux = perfilService.save(perfil);

        return perfAux;
    }

    /* Aprobar una reservacion de un perfil */
    @PatchMapping("/actividades/aprobarReser/{id}+{aprobar}+{idP}")
    @ResponseStatus(HttpStatus.CREATED)
    public Perfil aprobarReser(@PathVariable long id,
                             @PathVariable boolean aprobar, @PathVariable long idP) {
        Actividad auxAct = act.findById(id);
        Perfil perfil = perfilService.findById(idP);

        //Crea o quita la reservacion si el perfil ya reacciono
        auxAct.getCantidad().getReaccions().stream().forEach(reaccion -> {
            if (reaccion.getIdPerfil() == idP) {

                if (reaccion.isValReser() && aprobar) {
                    perfil.getReservacion().stream()
                            .filter(reservacion -> reservacion.getActividad().equals(auxAct))
                            .forEach(reservacion -> reservacion.setAprobacion(aprobar));

                    //--------------NOTIFICACION----------------------
                    NotificacionIMPL.crearNotifiPersonal(notifiProp.notifi[notifiProp.RESER_ACEPT],
                                    notifiProp.crearInfo(auxAct.getNombre(), notifiProp.RESER_ACEPT),
                                    perfil, auxAct, NotificacionIMPL.crearNotifi());

                    RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, idP,
                            Constant_RegAcciones.ESTADO_HECHA.name() + auxAct.getNombre(),
                            perfil.getNombre());

                } else if (reaccion.isValReser() && !aprobar) {
                    perfil.getReservacion().stream()
                            .filter(reservacion -> reservacion.getActividad().equals(auxAct))
                            .forEach(reservacion -> reservacion.setAprobacion(aprobar));

                    //--------------NOTIFICACION----------------------
                    NotificacionIMPL.crearNotifiPersonal(notifiProp.notifi[notifiProp.RESER_ANUL],
                                    notifiProp.crearInfo(auxAct.getNombre(), notifiProp.RESER_ANUL),
                                    perfil, auxAct, NotificacionIMPL.crearNotifi());

                    RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, idP,
                            Constant_RegAcciones.CANCELACION.name() + auxAct.getNombre(),
                            perfil.getNombre());

                }

            }
        });

        return perfilService.save(perfil);
    }

    /* Notificar de un nuevo evento */
    @PatchMapping("/actividades/nuevoEvent/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void nuevoEvent(@PathVariable long id){
        Actividad actividad = act.findById(id);
        NotificacionIMPL.crearNotifiGlobal(notifiProp.notifi[notifiProp.EVENT_NUEVO],
                notifiProp.crearInfo(actividad.getNombre(), notifiProp.EVENT_NUEVO),
                perfilService.findAll(),actividad, NotificacionIMPL.crearNotifi());
    }


//-----------------------DeleteMappings-----------------------------------

    /* Elimina una Actividad en especifico */
    @DeleteMapping("/actividades/deleteAct/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAct(@PathVariable Long id) {
        Actividad aux = findActById(id);
        List<Perfil> perfils = perfilService.findAll();

        RegistroAccionIMPL.crearReg(RegProp.TIPO_ACTIVIDAD, id,
                Constant_RegAcciones.ELIMINACION.name(), aux.getNombre());

        if (aux.getReservacion() != null) {
            for (Reservacion r :
                    aux.getReservacion()) {
                RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, id,
                        Constant_RegAcciones.ELIMINACION.name(), aux.getNombre());
            }
        }

        NotificacionIMPL.EliminarNotiAct(aux)
                .forEach(idN -> perfils.forEach(perfil -> perfil.getNotificacion()
                        .removeIf(notificacion -> notificacion.getId().equals(idN))
                ));

        perfilService.saveAll(perfils);
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
        List<Perfil> perfils = perfilService.findAll();

        RegistroAccionIMPL.crearReg(RegProp.TIPO_ACTIVIDAD, -1,
                Constant_RegAcciones.ELIMINACION.name(), "deleteAll");

        RegistroAccionIMPL.crearReg(RegProp.TIPO_RESERVACION, -1,
                Constant_RegAcciones.ELIMINACION.name(), "deleteAll");

        perfils.forEach(perfil -> perfil.getNotificacion().clear());

        perfilService.saveAll(perfils);
        NotificacionIMPL.EliminarNotiAct();
        act.deleteAll();
    }

//________________________________Metodos Auxiliares________________________________________________________________
    private void valMeEncanta(boolean add, Actividad actividad, Reaccion reaccion) {
        if (reaccion.isValMeEncanta() && !add) {
            actividad.getCantidad().setCantMeEncanta(actividad.getCantidad().getCantMeEncanta() - 1);
            reaccion.setValMeEncanta(add);

            RegistroAccionIMPL.crearReg(RegProp.TIPO_ACTIVIDAD, actividad.getId(),
                    Constant_RegAcciones.REACCION_ELIM.name(),
                    actividad.getNombre());

        } else if (!reaccion.isValMeEncanta() && add) {
            actividad.getCantidad().setCantMeEncanta(actividad.getCantidad().getCantMeEncanta() + 1);
            reaccion.setValMeEncanta(add);

            RegistroAccionIMPL.crearReg(RegProp.TIPO_ACTIVIDAD, actividad.getId(),
                    Constant_RegAcciones.REACCION_ADD.name(),
                    actividad.getNombre());

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

    //-------------------------------------------------
    //Activar hilos
    @GetMapping("/activarHilos")
    public void activarHilos(){
        if(bool){
            List<IHiloCentral> hiloCentralList = new ArrayList<>();

            hiloCentralList.add(new EventCadaHora(act, perfilService));
            hiloCentralList.add(new delOldAct(act, perfilService));
            hiloCentralList.add(new ReservAgotad(cantidadService, act, perfilService));

            hiloCentralList.forEach(hiloCentral -> hashMap.put(new Thread(hiloCentral), hiloCentral));

            bool = false;
        }
        AtomicInteger i = new AtomicInteger(0);
        hashMap.forEach((threads, hiloCentral) -> {
            if(!threads.isAlive())threads.start();
            if(threads != null && i.get() != -1)threads.setName(nameHilos[i.getAndIncrement()]);
        });
        i.set(-1);
    }

    //Desactivar hilos
    @GetMapping("/desactivarHilos")
    public void desactivarHilos(){
        HiloCentral.StopAll();

        hashMap.forEach((thread, hiloCentral) ->{
            if(thread.isAlive())thread.interrupt();
            if(thread.getName().equals("EventCadaHora") && ((EventCadaHora)hiloCentral).getThread().isAlive())((EventCadaHora)hiloCentral).getThread().interrupt();
            }
        );
        infoHilos();

    }

    @GetMapping("/detenerHilo/{name}")
    public void detenerHilo(@PathVariable String name){
         hashMap.forEach((thread, hiloCentral) -> {
             if(thread.getName().equals(name)) hiloCentral.setDetenerHilo();
             if(thread.getName().equals("EventCadaHora"))((EventCadaHora)hiloCentral).getThread().interrupt();
             thread.interrupt();
         }) ;
         infoHilos();
    }

    @GetMapping("/infoHilos")
    void infoHilos(){
        int j = hashMap.size()+1;
        AtomicInteger i = new AtomicInteger(0);

        hashMap.forEach((thread, hiloCentral) -> {
            if (thread.isAlive()) {
                System.out.println(thread.getName() + " -> Corriendo");
                if (thread.getName().equals("EventCadaHora")) {
                    System.out.println(((EventCadaHora) hiloCentral).getThread().getName() + " -> Corriendo");
                    i.getAndIncrement();
                }
                i.getAndIncrement();
            }
        });

        System.out.println("/////////////////////////////////");
        System.out.println(i + "/" + j + " Hilos Corriendo");
        System.out.println("/////////////////////////////////");

        i.set(0);

        hashMap.forEach((thread, hiloCentral) ->{
            if(!thread.isAlive()){
                System.out.println(thread.getName() + " -> Apagado");
                if (thread.getName().equals("EventCadaHora") && !((EventCadaHora) hiloCentral).getThread().isAlive()) {
                    System.out.println(((EventCadaHora) hiloCentral).getThread().getName() + " -> Apagado");
                    i.getAndIncrement();
                }
                i.getAndIncrement();
            }
        });

        System.out.println("/////////////////////////////////");
        System.out.println(i + "/" + j + " Hilos apagados");
        System.out.println("/////////////////////////////////");
    }

}