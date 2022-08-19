package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Dtos.PartidoAntoacionJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportesJugadoresService {

    @Autowired
    private SustitucionService sustitucionService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private JugadorPartidoService jugadorPartidoService;

    @Autowired
    private AnotacionService anotacionService;

    @Autowired
    private PartidoService partidoService;

    public PartidoAntoacionJugadorDto mapPartidoDeJugadorToPartidoAnotacionJugadorDto(Partido partido, Jugador jugador){
        PartidoAntoacionJugadorDto partidoAntoacionJugadorDto = new PartidoAntoacionJugadorDto(
                partido.getId(),
                partido.getFecha(),
                partido.getEstado().name(),
                partido.getClubLocal().getNombreClub(),
                partido.getClubVisitante().getNombreClub(),
                anotacionService.getListadoAnotacionesClubLocal(partido).size(),
                anotacionService.getListadoAnotacionesClubVisitante(partido).size(),
                partido.getJornada().getCompetencia().getNombre(),
                partido.getJornada().getDescripcion(),
                partido.getJornada().getCompetencia().getCategoria().getNombre(),
                jugadorPartidoService.getRolDeJugadorEnUnPartido(partido.getId(), jugador.getId()),
                jugadorPartidoService.clubDondeParticipoJugadorEnUnPartido(partido.getId(), jugador.getId()),
                sustitucionService.existeSustitucionPorPartidoYJugadorEntra(partido.getId(), jugador.getId()) ? "S":"N",
                anotacionService.anotacionesDeUnJugadorEnUnPartido(jugador.getId(), partido.getId()).size(),
                anotacionService.tipoDeAnotacionesDeJugadorEnUnPartido(partido.getId(), jugador.getId())
        );
        return partidoAntoacionJugadorDto;
    }
}
