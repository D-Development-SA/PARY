package PARY.services.implementacion;

import PARY.DAO.INotificacionDAO;
import PARY.entity.Actividad;
import PARY.entity.Notificacion;
import PARY.entity.Perfil;
import PARY.services.contratos.INotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionIMPL extends GenericsImpl<Notificacion, INotificacionDAO> implements INotificacionService {
    private static INotificacionDAO notifDao;
    @Autowired
    public NotificacionIMPL(INotificacionDAO dao) {
        super(dao);
        notifDao = super.DAO;
    }

    public static void crearNotifiPersonal(String titulo, String info, Perfil perfil, Actividad actividad, long id){
        Notificacion notifi = notifDao.findById(id).orElse(null);

        if (perfil.getDireccion().getMunicipio().equals(actividad.getDireccion().getMunicipio())
                && perfil.getDireccion().getProvincia().equals(actividad.getDireccion().getProvincia())
                && notifi != null) {
            notifi.setTitulo(titulo);
            notifi.setInfo(info);
            notifi.setActividad(actividad);
            perfil.getNotificacion().add(notifi);
            notifDao.save(notifi);

        }
    }
    public static void crearNotifiGlobal(String titulo, String info, List<Perfil> perfil, Actividad actividad, long id){
        Notificacion notifi = notifDao.findById(id).orElse(null);

        if (notifi != null) {
            notifi.setTitulo(titulo);
            notifi.setInfo(info);
            notifi.setActividad(actividad);
            perfil.stream().parallel()
                    .filter(perfil1 -> perfil1.getDireccion().getMunicipio().equals(actividad.getDireccion().getMunicipio()))
                    .forEach(perfil1 -> perfil1.getNotificacion().add(notifi));
            notifDao.save(notifi);
        }
    }

    public static long crearNotifi(){
        Notificacion notifi = new Notificacion();
        return notifDao.save(notifi).getId();
    }

    public static void EliminarNotiAct(){
        notifDao.deleteAll();

    }
    public static List<Long> EliminarNotiAct(Actividad actividad){
         List<Notificacion> notificacions = (List<Notificacion>) notifDao.findAll();
         return notificacions.stream()
                 .parallel()
                 .filter(notificacion -> notificacion.getActividad().equals(actividad))
                 .map(Notificacion::getId)
                 .toList();
    }

}
