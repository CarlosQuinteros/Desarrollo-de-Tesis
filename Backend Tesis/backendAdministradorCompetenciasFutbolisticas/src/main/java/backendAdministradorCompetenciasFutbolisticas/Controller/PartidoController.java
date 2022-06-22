package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Dtos.PartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.JuezRol;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.JuezRolService;
import backendAdministradorCompetenciasFutbolisticas.Service.LogService;
import backendAdministradorCompetenciasFutbolisticas.Service.PartidoService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/partidos")
@CrossOrigin("*")
public class PartidoController {

    @Autowired
    private ClubService clubService;

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private LogService logService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JuezRolService juezRolService;

    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearPartido(@Valid @RequestBody PartidoDto nuevoPartido, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        LocalDateTime fecha = LocalDateTime.now();
        System.out.println("sout localdatetime: " + fecha);
        System.out.println("sout to localdate: " + fecha.toLocalDate());
        System.out.println("sout toString: " + fecha.toString());
        Club clubLocal = clubService.getClub(nuevoPartido.getIdClubLocal());
        Club clubVisitante = clubService.getClub(nuevoPartido.getIdClubVisitante());

        Partido partido = new Partido(fecha, nuevoPartido.getObservaciones(), clubLocal, clubVisitante);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        partidoService.guardarPartido(partido);
        logService.guardarLogCreacionPartido(partido,usuario);
        return new ResponseEntity<>(new Mensaje("Partido guardado correctamente", partido), HttpStatus.CREATED);
    }

    @GetMapping("/detalle/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<Partido> detallePartido(@PathVariable("id") Long id){
        Partido partido = partidoService.getDetallePartido(id);
        return new ResponseEntity<>(partido,HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/jueces")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<JuezRol>> listadoDeJuecesDePartido(@PathVariable ("id") Long id){
        List<JuezRol> jueces = juezRolService.getParticipacionesPorIdPartido(id);
        return new ResponseEntity<>(jueces, HttpStatus.OK);
    }



    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> eliminarPartido(@PathVariable ("id") Long id){
        if(!partidoService.existePartidoPorId(id)){
            throw new BadRequestException("No existe el partido con ID: " + id);
        }
        partidoService.eliminarPartido(id);
        return new ResponseEntity<>(new Mensaje("Partido eliminado correctamente"),HttpStatus.OK);
    }

    @PutMapping("/{id}/finalizado")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> establecerPartidoComoFinalizado(@PathVariable ("id") Long id){
        partidoService.finalizarPartido(id);
        return new ResponseEntity<>(new Mensaje("Partido finalizado correctamente"),HttpStatus.OK);
    }


}
