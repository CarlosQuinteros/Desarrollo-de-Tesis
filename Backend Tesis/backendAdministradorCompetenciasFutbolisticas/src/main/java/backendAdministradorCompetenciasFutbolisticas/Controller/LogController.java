package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Entity.Log;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.RecursoNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@CrossOrigin("*")
public class LogController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    LogService logService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listado")
    public ResponseEntity<List<Log>> listadoLogs(){
        List<Log> listado = logService.getListado();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/actividadUsuario/{idUsuario}")
    public ResponseEntity<List<Log>> listadologsPorUsuario(@PathVariable ("idUsuario") Long idUsuario){
        if(!usuarioService.existById(idUsuario)) throw new RecursoNotFoundException("El usuario con ID: " + idUsuario +" no existe");
        List<Log> listado = logService.logsPorUsuario(idUsuario);
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/actividad/{username}")
    public ResponseEntity<List<Log>> listadoLogsPorNombreDeUsuario(@PathVariable ("username") String username){
        if(!usuarioService.existByNombreUsuario(username)) throw new RecursoNotFoundException("El usuario no existe");
        List<Log> listado = logService.logsPorNombreDeUsuario(username);
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }
}
