package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Dtos.PartidoAntoacionJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Interface.IGoleador;
import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoPartido;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.AnotacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnotacionService {

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private JugadorPartidoService jugadorPartidoService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private AnotacionRepository anotacionRepository;

    @Autowired
    private SustitucionService sustitucionService;

    @Autowired
    private CompetenciaService competenciaService;


    public Anotacion guardarAnotacion(Anotacion nuevaAnotacion){
        Partido partido = partidoService.getDetallePartido(nuevaAnotacion.getPartido().getId());
        Club clubAnota = clubService.getClub(nuevaAnotacion.getClubAnota().getId());
        Jugador jugadorAnota = jugadorService.getJugador(nuevaAnotacion.getJugador().getId());
        if(!partidoService.clubFormaParteDePartido(partido,clubAnota)){
            throw new BadRequestException("El club "+ clubAnota.getNombreClub() + " no forma parte del partido");
        }
        if(sustitucionService.existeSustitucionPorPartidoYJugadorSale(partido.getId(),jugadorAnota.getId())){
            throw new BadRequestException("El jugador que anota ya fue sustituido y no puede anotar");
        }
        if(nuevaAnotacion.getTipoGol().equals(NombreTipoGol.GOL_EN_CONTRA) && !jugadorFormaParteDelEquipoContrarioAlQueAnota(partido,clubAnota,jugadorAnota)){
            throw new BadRequestException("Para guardar un gol en contra, el jugador " + jugadorAnota.getApellidos() + ", " + jugadorAnota.getNombre() + " debe ser titular o haber entrado en una sustitucion del equipo contrario");
        }
        if(!nuevaAnotacion.getTipoGol().equals(NombreTipoGol.GOL_EN_CONTRA) && !jugadorAnotaEsTitularOIngresoEnSustitucion(partido.getId(), clubAnota.getId(),jugadorAnota.getId())){
            throw new BadRequestException("El jugador debe formar parte de los titulares de " + clubAnota.getNombreClub() + " o haber ingresado en una sustitucion");
        }
        return anotacionRepository.save(nuevaAnotacion);
    }

    public List<NombreTipoGol> getListadoTipoAnotaciones(){
        return Arrays.stream(NombreTipoGol.values()).collect(Collectors.toList());
    }

    public List<String> getListadoStringTipoAnotaciones(){
        return getListadoTipoAnotaciones().stream()
                .map(NombreTipoGol::name)
                .collect(Collectors.toList());
    }

    public NombreTipoGol getTipoAnotacionPorNombre(String nombre){
        return getListadoTipoAnotaciones().stream()
                .filter(tipoGol -> tipoGol.name().equals(nombre))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("No existe el tipo de gol: " + nombre));
    }

    public Anotacion getAnotacion(Long id){
        return anotacionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No existe la anotacion con Id: " + id));
    }

    public void eliminarAnotacion(Long id){
        Anotacion anotacion = getAnotacion(id);
        anotacionRepository.deleteById(anotacion.getId());
    }

    public List<Anotacion> getListadoAnotacionesPorPartidoYClub(Long idPartido, Long idClub){
        return anotacionRepository.findByPartido_IdAndClubAnota_Id(idPartido,idClub);
    }

    public List<Anotacion> getListadoAnotacionesClubLocal(Partido partido){
        return getListadoAnotacionesPorPartidoYClub(partido.getId(), partido.getClubLocal().getId());
    }

    public List<Anotacion> getListadoAnotacionesClubVisitante(Partido partido){
        return getListadoAnotacionesPorPartidoYClub(partido.getId(), partido.getClubVisitante().getId());
    }

    public List<Anotacion> getListadoTodasLasAnotacionesDeUnJugador(Long idJugador){
        return anotacionRepository.findByJugador_IdAndTipoGolNot(idJugador, NombreTipoGol.GOL_EN_CONTRA);
    }

    public boolean jugadorFormaParteDelEquipoContrarioAlQueAnota(Partido partido, Club clubAnota, Jugador jugador ){
        Club clubContrario = partido.getClubLocal().getId().equals(clubAnota.getId())? partido.getClubVisitante() : partido.getClubLocal();
        return jugadorAnotaEsTitularOIngresoEnSustitucion(partido.getId(),clubContrario.getId(),jugador.getId());
    }

    public boolean jugadorAnotaEsTitularOIngresoEnSustitucion(Long idPartido, Long idCub, Long idJugador){
        return jugadorPartidoService.jugadorFormaParteDeTitulares(idPartido,idCub,idJugador)
                || sustitucionService.existeSustitucionPorPartidoYClubYJugadorEntra(idPartido,idCub,idJugador);
    }

    public boolean existeAnotacionEnPartidoDeJugador(Long idPartido, Long idJugador){
        return anotacionRepository.existsByPartido_IdAndJugador_Id(idPartido, idJugador);
    }

    public List<Anotacion> anotacionesDeUnaCompetencia(Long idCompetencia){
        Competencia competencia = competenciaService.getCompetencia(idCompetencia);
        return anotacionRepository.findByPartidoJornadaCompetencia_Id(competencia.getId());
    }

    public List<IGoleador> goleadoresDeUnaCompetencia(Long idCompetencia){
        return anotacionRepository.findGoleadoresByCompetencia_Id(idCompetencia);
    }

    public List<Anotacion> anotacionesDeUnJugadorEnUnPartido(Long idJugador, Long idPartido){
        return anotacionRepository.findByPartido_IdAndJugador_Id(idPartido, idJugador)
                .stream()
                .filter(anotacion -> anotacion.getPartido().getEstado().equals(NombreEstadoPartido.FINALIZADO) )
                .filter(anotacion -> !anotacion.getTipoGol().equals(NombreTipoGol.GOL_EN_CONTRA))
                .collect(Collectors.toList());
    }

    public List<String> tipoDeAnotacionesDeJugadorEnUnPartido(Long idPartido, Long idJugador){
        List<String> tipoGoles = anotacionesDeUnJugadorEnUnPartido(idJugador,idPartido)
                .stream()
                .map(anotacion -> anotacion.getTipoGol().name())
                .collect(Collectors.toList());
        return  tipoGoles;
    }





}
