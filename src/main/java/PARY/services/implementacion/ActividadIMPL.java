package PARY.services.implementacion;

import PARY.DAO.IActividadDAO;
import PARY.entity.Actividad;
import PARY.entity.Reservacion;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.services.contratos.IActividadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class ActividadIMPL extends GenericsImpl<Actividad, IActividadDAO> implements IActividadesService {

    @Autowired
    public ActividadIMPL(IActividadDAO dao) {
        super(dao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Actividad> findActividadesByNombreContains(String nombre) {
        return DAO.findActividadesByNombreContains(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Actividad> showRecentAct() {
        return DAO.findActividadesByOrderByFechaHoraDesc();
    }

    @Override
    @Transactional
    public void deleteOldAct() {
        String fecha = LocalDate.now().toString();
        int fechaNum = Integer.parseInt(fecha.replaceAll("-",""));
        HashMap<Long, Integer> fechaMap = new HashMap<>();

        List<Actividad> actividades = findAll();

        actividades.forEach(a-> fechaMap.put(a.getId(),
                Integer.parseInt(a.getFechaHora().
                        substring(0, 10).
                        replaceAll("-",""))));

        fechaMap.forEach((a,b)->{
            if (b >= fechaNum) {
                actividades.removeIf(c-> c.getId() == a);
            }
        });

        actividades.forEach(a -> {
            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD,
                    a.getId(),
                    Constant_RegAcciones.ELIMINACION,
                    a.getNombre());

            if (a.getReservacion() != null) {
                for (Reservacion d :
                        a.getReservacion()) {
                    RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION,
                            d.getId(),
                            Constant_RegAcciones.ELIMINACION,
                            a.getNombre());
                }
            }
        });
        DAO.deleteAll(actividades);
    }

}
