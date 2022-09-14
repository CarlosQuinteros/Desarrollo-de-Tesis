package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.PartidoAntoacionJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.ParticipacionesJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Anotacion;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorPartido;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoPartido;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;
import backendAdministradorCompetenciasFutbolisticas.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private SustitucionService sustitucionService;

    @Autowired
    private AnotacionService anotacionService;

    @Autowired
    private ReportesJugadoresService reportesJugadoresService;

    @GetMapping("/dni/{documento}/participaciones")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<ParticipacionesJugadorDto> participacionesDeJugador(@PathVariable String documento){

        Jugador jugador = jugadorService.getJugadorPorDni(documento);
        List<JugadorPartido> todasLasParticipaciones = jugadorPartidoService.getParticipacionesDeJugador(jugador)
                .stream()
                .filter(jugadorPartido -> jugadorPartido.getPartido().getEstado().equals(NombreEstadoPartido.FINALIZADO))
                .sorted(Comparator.comparing(jugadorPartido -> jugadorPartido.getPartido().getFecha()))
                .collect(Collectors.toList());
        DetalleGeneralPartidoDto ultimoPartido = (todasLasParticipaciones.isEmpty()) ? null
                : partidoService.mapPartidoToDetalleGeneralPartidoDto(todasLasParticipaciones.get(todasLasParticipaciones.size()-1).getPartido());
        ParticipacionesJugadorDto participaciones = new ParticipacionesJugadorDto();
        participaciones.setParticipaciones(todasLasParticipaciones);
        participaciones.setUltimoPartido(ultimoPartido);
        return new ResponseEntity<>(participaciones, HttpStatus.OK);
    }

    @GetMapping("/dni/{documento}/efectividad")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> efectividadDeUnJugador(@PathVariable String documento){
        Jugador jugador = jugadorService.getJugadorPorDni(documento);
        List<Partido> partidosDelJugador = jugadorPartidoService.getParticipacionesDeJugador(jugador)
            .stream()
                .filter(jugadorPartido -> jugadorPartido.getPartido().getEstado().equals(NombreEstadoPartido.FINALIZADO))
            .map(JugadorPartido::getPartido)
            .collect(Collectors.toList());

        List<PartidoAntoacionJugadorDto> partidoAntoacionJugadorDtoList = partidosDelJugador.stream()
                .map(partido -> reportesJugadoresService.mapPartidoDeJugadorToPartidoAnotacionJugadorDto(partido, jugador))
                .collect(Collectors.toList());
        return new ResponseEntity<>(partidoAntoacionJugadorDtoList,HttpStatus.OK);
    }

    @GetMapping("/dni/{documento}/goles-mas-anotados")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> golesMasAnotadosDeJugador(@PathVariable String documento){
        Jugador jugador = jugadorService.getJugadorPorDni(documento);
        List<Anotacion> todasLasAnotaciones = anotacionService.getListadoTodasLasAnotacionesDeUnJugador(jugador.getId());
        Map<String, Long> golesMasAnotados = todasLasAnotaciones.stream()
                .filter(anotacion -> anotacion.getPartido().getEstado().equals(NombreEstadoPartido.FINALIZADO))
                .filter(anotacion -> !anotacion.getTipoGol().equals(NombreTipoGol.GOL_EN_CONTRA))
                .collect(Collectors.groupingBy(anotacion -> anotacion.getTipoGol().name(), Collectors.counting()));

        return new ResponseEntity<>(golesMasAnotados,HttpStatus.OK);
    }

    @GetMapping("/dni/{documento}/anotaciones-anuales")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> anotacionesAnualesDeJugador(@PathVariable String documento){
        Jugador jugador = jugadorService.getJugadorPorDni(documento);
        List<Anotacion> todasLasAnotaciones = anotacionService.getListadoTodasLasAnotacionesDeUnJugador(jugador.getId());
        Map<Integer, Long> golesAnuales = todasLasAnotaciones.stream()
                .filter(anotacion -> anotacion.getPartido().getEstado().equals(NombreEstadoPartido.FINALIZADO))
                .filter(anotacion -> !anotacion.getTipoGol().equals(NombreTipoGol.GOL_EN_CONTRA))
                .collect(Collectors.groupingBy(anotacion -> anotacion.getPartido().getFecha().toLocalDate().getYear(), Collectors.counting()));

        return new ResponseEntity<>(golesAnuales,HttpStatus.OK);
    }

}
