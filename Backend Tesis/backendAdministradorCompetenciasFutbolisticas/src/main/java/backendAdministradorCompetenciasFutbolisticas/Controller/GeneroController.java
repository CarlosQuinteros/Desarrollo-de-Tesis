package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Service.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/generos")
@CrossOrigin("*")
public class GeneroController {

    @Autowired
    private GeneroService generoService;


    @GetMapping("/tipos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<String>> listadoDeGeneros(){
        List<String> generos = generoService.getListadoStringTipoGenero();
        return new ResponseEntity<>(generos, HttpStatus.OK);
    }
}
