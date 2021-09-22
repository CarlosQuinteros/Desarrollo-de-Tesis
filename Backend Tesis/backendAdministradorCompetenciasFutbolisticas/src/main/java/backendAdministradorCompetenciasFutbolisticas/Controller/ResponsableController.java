package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Entity.Responsable;
import backendAdministradorCompetenciasFutbolisticas.Repository.ResponsableRepository;
import backendAdministradorCompetenciasFutbolisticas.Service.ResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/responsables")
@CrossOrigin("*")
public class ResponsableController {

    @Autowired
    ResponsableService responsableService;
    /*
        Metodo que retorna el listado de responsables de  clubes
     */
    @GetMapping("/listado")
    public ResponseEntity<List<Responsable>> listadoResponsables(){
        List<Responsable> listado = responsableService.getListadoOrdenadoPorApellido();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }
}