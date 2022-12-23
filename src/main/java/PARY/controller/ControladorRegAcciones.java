package PARY.controller;

import PARY.entity.RegistroAccion;
import PARY.services.contratos.IRegistroAccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControladorRegAcciones {
    @Autowired
    private IRegistroAccionService reg;

//-----------------------GetMappings-----------------------------------

    /* Devuelve todos las Registros */
    @GetMapping("/registro")
    @ResponseStatus(HttpStatus.OK)
    public List<RegistroAccion> findAll(){
        return reg.findAll();
    }

//-----------------------DeleteMappings-----------------------------------

    /* Elimina un Registro en especifico */
    @DeleteMapping("/registro/eliminarRegId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRegById(@PathVariable long id){
        reg.deleteById(id);
    }

    /* Elimina todos las Registros */
    @DeleteMapping("/registro/eliminarAllReg")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllReg(){
        reg.deleteAll();
    }
}
