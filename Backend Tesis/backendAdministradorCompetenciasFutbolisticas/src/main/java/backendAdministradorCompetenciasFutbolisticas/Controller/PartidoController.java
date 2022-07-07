package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Dtos.PartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.*;
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

    @Autowired
    private JugadorPartidoService jugadorPartidoService;

    @Autowired
    private SustitucionService sustitucionService;

    @Autowired
    private AnotacionService anotacionService;

    @Autowired
    private JornadaService jornadaService;

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
        Jornada jornada = jornadaService.getJornada(nuevoPartido.getIdJornada());
        Partido partido = new Partido(fecha, nuevoPartido.getObservaciones(), clubLocal, clubVisitante, jornada);

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

    @GetMapping("/{id}/detalle-general")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<DetalleGeneralPartidoDto> detalleDatosGeneralesPartido(@PathVariable("id") Long id){
        Partido partido = partidoService.getDetallePartido(id);
        DetalleGeneralPartidoDto detalleGeneralPartidoDto = partidoService.mapPartidoToDetalleGeneralPartidoDto(partido);
        return new ResponseEntity<>(detalleGeneralPartidoDto,HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/jueces")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<JuezRol>> listadoDeJuecesDePartido(@PathVariable ("id") Long id){
        List<JuezRol> jueces = juezRolService.getParticipacionesPorIdPartido(id);
        return new ResponseEntity<>(jueces, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/club-local/titulares")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<JugadorPartido>>listadoDeTitularesDelClubLocal(@PathVariable ("id") Long id){
        Partido partido = partidoService.getDetallePartido(id);
        List<JugadorPartido> titularesClubLocal = jugadorPartidoService.getListadoJugadoresTitularesClubLocal(partido);
        return new ResponseEntity<>(titularesClubLocal,HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/club-local/suplentes")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<JugadorPartido>>listadoDeSuplentesDelClubLocal(@PathVariable ("id") Long id){
        Partido partido = partidoService.getDetallePartido(id);
        List<JugadorPartido> suplentesClubLocal = jugadorPartidoService.getListadoJugadoresSuplentesClubLocal(partido);
        return new ResponseEntity<>(suplentesClubLocal,HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/club-visitante/titulares")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<JugadorPartido>>listadoDeTitularesDelClubVisitante(@PathVariable ("id") Long id){
        Partido partido = partidoService.getDetallePartido(id);
        List<JugadorPartido> titularesClubVisitante = jugadorPartidoService.getListadoJugadoresTitularesClubVisitante(partido);
        return new ResponseEntity<>(titularesClubVisitante,HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/club-visitante/suplentes")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<JugadorPartido>>listadoDeSuplentesDelClubVisitante(@PathVariable ("id") Long id){
        Partido partido = partidoService.getDetallePartido(id);
        List<JugadorPartido> suplentesClubVisitante = jugadorPartidoService.getListadoJugadoresSuplentesClubVisitante(partido);
        return new ResponseEntity<>(suplentesClubVisitante,HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/club-local/sustituciones")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<Sustitucion>> listadoSustitucionesDelClubLocal(@PathVariable ("id") Long id){
        Partido partido = partidoService.getDetallePartido(id);
        List<Sustitucion> sustituciones = sustitucionService.getListadoSustitucionesClubLocal(partido);
        return new ResponseEntity<>(sustituciones,HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/club-visitante/sustituciones")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<Sustitucion>> listadoSustitucionesDelClubVisitante(@PathVariable ("id") Long id){
        Partido partido = partidoService.getDetallePartido(id);
        List<Sustitucion> sustituciones = sustitucionService.getListadoSustitucionesClubVisitante(partido);
        return new ResponseEntity<>(sustituciones,HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}/club-local/anotaciones")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<Anotacion>> listadoAnotacionesDelClubLocal(@PathVariable Long id){
        Partido partido = partidoService.getDetallePartido(id);
        List<Anotacion> anotaciones = anotacionService.getListadoAnotacionesClubLocal(partido);
        return new ResponseEntity<>(anotaciones,HttpStatus.OK);
    }
    @GetMapping("/detalle/{id}/club-visitante/anotaciones")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<Anotacion>> listadoAnotacionesDelClubVisitante(@PathVariable Long id){
        Partido partido = partidoService.getDetallePartido(id);
        List<Anotacion> anotaciones = anotacionService.getListadoAnotacionesClubVisitante(partido);
        return new ResponseEntity<>(anotaciones,HttpStatus.OK);
    }


    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> eliminarPartido(@PathVariable ("id") Long id){
        partidoService.eliminarPartido(id);
        return new ResponseEntity<>(new Mensaje("Partido eliminado correctamente"),HttpStatus.OK);
    }

    @PutMapping("/{id}/finalizado")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> establecerPartidoComoFinalizado(@PathVariable ("id") Long id){
        partidoService.finalizarPartido(id);
        return new ResponseEntity<>(new Mensaje("Partido finalizado correctamente"),HttpStatus.OK);
    }

    @PutMapping("/{id}/pendiente")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> establecerPartidoComoPendiente(@PathVariable Long id){
        partidoService.finalizarPartido(id);
        return new ResponseEntity<>(new Mensaje("Partido establecido como 'Pendiente' correctamente"),HttpStatus.OK);
    }


}
