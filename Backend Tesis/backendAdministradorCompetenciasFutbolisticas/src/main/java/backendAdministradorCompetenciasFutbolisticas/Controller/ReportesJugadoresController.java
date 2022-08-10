package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.ParticipacionesJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorPartido;
import backendAdministradorCompetenciasFutbolisticas.Service.JugadorPartidoService;
import backendAdministradorCompetenciasFutbolisticas.Service.JugadorService;
import backendAdministradorCompetenciasFutbolisticas.Service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reportes-jugadores")
@CrossOrigin("*")
public class ReportesJugadoresController {

    @Autowired
    private JugadorPartidoService jugadorPartidoService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private PartidoService partidoService;

    @GetMapping("/dni/{documento}/participaciones")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<ParticipacionesJugadorDto> participacionesDeJugador(@PathVariable String documento){

        Jugador jugador = jugadorService.getJugadorPorDni(documento);
        List<JugadorPartido> todasLasParticipaciones = jugadorPartidoService.getParticipacionesDeJugador(jugador)
                .stream()
                .sorted(Comparator.comparing(jugadorPartido -> jugadorPartido.getPartido().getFecha()))
                .collect(Collectors.toList());
        DetalleGeneralPartidoDto ultimoPartido = (todasLasParticipaciones.isEmpty()) ? null
                : partidoService.mapPartidoToDetalleGeneralPartidoDto(todasLasParticipaciones.get(todasLasParticipaciones.size()-1).getPartido());
        ParticipacionesJugadorDto participaciones = new ParticipacionesJugadorDto();
        participaciones.setParticipaciones(todasLasParticipaciones);
        participaciones.setUltimoPartido(ultimoPartido);
        return new ResponseEntity<>(participaciones, HttpStatus.OK);
    }


}
