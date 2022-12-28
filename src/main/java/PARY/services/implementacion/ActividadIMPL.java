package PARY.services.implementacion;

import PARY.DAO.IMisActividadDAO;
import PARY.entity.misActividades;
import PARY.entity.constantes.Constant_RegAcciones;
import PARY.services.contratos.IMisActividadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class misActividadIMPL extends GenericsImpl<misActividades, IMisActividadDAO> implements IMisActividadesService {

    @Autowired
    public misActividadIMPL(IMisActividadDAO dao) {
        super(dao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<misActividades> findActividadesByNombreContains(String nombre) {
        return DAO.findActividadesByNombreContains(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<misActividades> showRecentAct() {
        return DAO.findActividadesByOrderByFechaHoraDesc();
    }

    @Override
    @Transactional
    public void deleteOldAct() {
        String fecha = LocalDate.now().toString();
        int fechaNum = Integer.parseInt(fecha.replaceAll("-",""));
        HashMap<Long, Integer> fechaMap = new HashMap<>();

        List<misActividades> actividades = findAll();

        actividades.forEach(a-> fechaMap.put(a.getId(),
                Integer.parseInt(a.getFechaHora().
                        substring(0, 10).
                        replaceAll("-",""))));

        fechaMap.forEach((a,b)->{
            if (b >= fechaNum) {
                actividades.removeIf(c-> c.getId() == a);
            }
        });
        actividades.forEach(a->{
                        RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_ACTIVIDAD,
                                                    a.getId(),
                                                    Constant_RegAcciones.ELIMINACION,
                                                    a.getNombre());

                        if(a.getReservacion() != null)
                            RegistroAccionIMPL.crearReg(RegistroAccionIMPL.TIPO_RESERVACION,
                                                        a.getReservacion().getId(),
                                                        Constant_RegAcciones.ELIMINACION,
                                                        a.getNombre());
                    });
        DAO.deleteAll(actividades);
    }

}
