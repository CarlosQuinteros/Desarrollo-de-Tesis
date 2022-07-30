package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.JugadorPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorPartido;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Enums.PosicionJugador;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoRolJugador;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.JugadorPartidoService;
import backendAdministradorCompetenciasFutbolisticas.Service.JugadorService;
import backendAdministradorCompetenciasFutbolisticas.Service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/participaciones-jugadores")
@CrossOrigin("*")
public class JugadorPartidoController {

    @Autowired
    private JugadorPartidoService jugadorPartidoService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private ClubService clubService;

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    @PostMapping("/titular")
    public ResponseEntity<?> crearParticipacionJugadorTitular(@Valid @RequestBody JugadorPartidoDto nuevoTitular, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Partido partido = partidoService.getDetallePartido(nuevoTitular.getIdPartido());
        Club club = clubService.getClub(nuevoTitular.getIdClub());
        if(!partidoService.clubFormaParteDePartido(partido,club)){
            throw new BadRequestException("El club " + club.getNombreClub() + " no forma parte del partido");
        }
        Jugador jugador = jugadorService.getJugador(nuevoTitular.getIdJugador());
        PosicionJugador posicion = jugadorPartidoService.getPosicionJugadorPorNombre(nuevoTitular.getPosicion());
        JugadorPartido nuevoJugadorRolTitular = new JugadorPartido(partido,club,jugador, TipoRolJugador.TITULAR,nuevoTitular.getNroCamiseta(),posicion);
        jugadorPartidoService.guardarParticipacionJugadorTitular(nuevoJugadorRolTitular);
        return new ResponseEntity<>(new Mensaje("Jugador Titular guardado correctamente"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    @PostMapping("/suplente")
    public ResponseEntity<?> crearParticipacionJugadorSuplente(@Valid @RequestBody JugadorPartidoDto nuevoSuplente, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Partido partido = partidoService.getDetallePartido(nuevoSuplente.getIdPartido());
        Club club = clubService.getClub(nuevoSuplente.getIdClub());
        if(!partidoService.clubFormaParteDePartido(partido,club)){
            throw new BadRequestException("El club " + club.getNombreClub() + " no forma parte del partido");
        }
        Jugador jugador = jugadorService.getJugador(nuevoSuplente.getIdJugador());
        PosicionJugador posicion = jugadorPartidoService.getPosicionJugadorPorNombre(nuevoSuplente.getPosicion());
        JugadorPartido nuevoJugadorRolSuplente = new JugadorPartido(partido,club,jugador, TipoRolJugador.SUPLENTE,nuevoSuplente.getNroCamiseta(),posicion);
        jugadorPartidoService.guardarParticipacionJugadorSuplente(nuevoJugadorRolSuplente);
        return new ResponseEntity<>(new Mensaje("Jugador Suplente guardado correctamente"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> EditarParticipacionJugador(@PathVariable("id") Long id, @Valid @RequestBody JugadorPartidoDto participacionJugadorDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        PosicionJugador posicion = jugadorPartidoService.getPosicionJugadorPorNombre(participacionJugadorDto.getPosicion());
        JugadorPartido participacionJugador = jugadorPartidoService.getJugadorPartidoById(id);
        participacionJugador.setNroCamiseta(participacionJugadorDto.getNroCamiseta());
        participacionJugador.setPosicion(posicion);
        jugadorPartidoService.guardarParticipacionJugador(participacionJugador);
        return new ResponseEntity<>(new Mensaje("Participacion guardada correctamente"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarParticipacionJugador(@PathVariable Long id){
        jugadorPartidoService.eliminarParticipacionJugador(id);
        return new ResponseEntity<>(new Mensaje("Participacion eliminada correctamente"),HttpStatus.OK);
    }

    @GetMapping("/posiciones")
    public ResponseEntity<List<String>> listadoDePosiciones(){
        List<String> posiciones = jugadorPartidoService.getListadoStringPosiciones();
        return new ResponseEntity<>(posiciones, HttpStatus.OK);
    }

}
