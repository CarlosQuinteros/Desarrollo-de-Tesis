package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorPartido;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Entity.Sustitucion;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoRolJugador;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.SustitucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SustitucionService {

    @Autowired
    private SustitucionRepository sustitucionRepository;

    @Autowired
    private JugadorPartidoService jugadorPartidoService;

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private ClubService clubService;

    public Sustitucion guardarSustitucion(Sustitucion sustitucion){
        if(!partidoService.clubFormaParteDePartido(sustitucion.getPartido(), sustitucion.getClubSustituye())){
            throw new BadRequestException("El club que sustituye no forma parte del partido");
        }
        if(sustitucion.getJugadorSale().getId().equals(sustitucion.getJugadorEntra().getId())){
            throw new BadRequestException("El jugador que entra y sale no deben ser los mismos");
        }
        /*if(!jugadorPartidoService.jugadorFormaParteDelEquipoEnPartido(sustitucion.getPartido().getId(), sustitucion.getClubSustituye().getId(), sustitucion.getJugadorEntra().getId())){
            throw new BadRequestException("El jugador que entra no forma parte del equipo del club " + sustitucion.getClubSustituye().getNombreClub());
        }
        if(!jugadorPartidoService.jugadorFormaParteDelEquipoEnPartido(sustitucion.getPartido().getId(), sustitucion.getClubSustituye().getId(), sustitucion.getJugadorSale().getId())){
            throw new BadRequestException("El jugador que sale no forma parte del equipo del club " + sustitucion.getClubSustituye().getNombreClub());
        }*/
        if(existeSustitucionPorPartidoYClubYJugadorSale(sustitucion.getPartido().getId(),sustitucion.getClubSustituye().getId(), sustitucion.getJugadorSale().getId())){
            throw new BadRequestException("El jugador que sale ya fue sustituido");
        }
        if(existeSustitucionPorPartidoYClubYJugadorEntra(sustitucion.getPartido().getId(), sustitucion.getClubSustituye().getId(),sustitucion.getJugadorEntra().getId())){
            throw new BadRequestException("El jugador que entra ya fue ingresado");
        }
        if(!jugadorPartidoService.jugadorFormaParteDeSuplentes(sustitucion.getPartido().getId(),sustitucion.getClubSustituye().getId(),sustitucion.getJugadorEntra().getId())){
            throw new BadRequestException("El jugador que entra debe formar parte de los suplentes");
        }
        if(!jugadorSaleFormaParteDeTitularesOIngresoEnUnaSustitucion(sustitucion.getPartido().getId(),sustitucion.getClubSustituye().getId(), sustitucion.getJugadorSale().getId())){
            throw new BadRequestException("El jugador que sale debe formar parte de los titulares o haber entrado previamente para ser reemplazado");
        }

        return sustitucionRepository.save(sustitucion);
    }

    public Sustitucion getDetalleSustitucion(Long id){
        return sustitucionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La sustitución con Id: " + id + " no existe"));
    }

    public void eliminarSustitucion(Long id){
        sustitucionRepository.deleteById(id);
    }

    public boolean existeSustitucionPorPartidoYClubYJugadorSale(Long idPartido, Long idClubSustituye, Long idJugadorSale){
        return sustitucionRepository.existsByPartido_IdAndClubSustituye_IdAndJugadorSale_Id(idPartido,idClubSustituye,idJugadorSale);
    }

    public boolean existeSustitucionPorPartidoYClubYJugadorEntra(Long idPartido, Long idClubSustituye, Long idJugadorEntra){
        return sustitucionRepository.existsByPartido_IdAndClubSustituye_IdAndJugadorEntra_Id(idPartido, idClubSustituye, idJugadorEntra);
    }

    public boolean existeSustitucionPorPartidoYJugadorSale(Long idPartido, Long idJugadorSale){
        return sustitucionRepository.existsByPartido_IdAndJugadorSale_Id(idPartido,idJugadorSale);
    }

    public boolean existeSustitucionPorPartidoYJugadorEntra(Long idPartido, Long idJugadorEntra){
        return sustitucionRepository.existsByPartido_IdAndJugadorEntra_Id(idPartido, idJugadorEntra);
    }

    public boolean jugadorSaleFormaParteDeTitularesOIngresoEnUnaSustitucion(Long idPartido, Long idClubSustituye, Long idJugador){
        return jugadorPartidoService.jugadorFormaParteDeTitulares(idPartido,idClubSustituye,idJugador)
                || existeSustitucionPorPartidoYClubYJugadorEntra(idPartido, idClubSustituye, idJugador);
    }

    public List<Sustitucion> getListadoSustitucionesPorPartidoYClub(Long idPartido, Long idClub){
        return sustitucionRepository.findByPartido_IdAndClubSustituye_Id(idPartido, idClub);
    }

    public List<Sustitucion> getListadoSustitucionesClubLocal(Partido partido){
        return getListadoSustitucionesPorPartidoYClub(partido.getId(), partido.getClubLocal().getId());
    }

    public List<Sustitucion> getListadoSustitucionesClubVisitante(Partido partido){
        return getListadoSustitucionesPorPartidoYClub(partido.getId(), partido.getClubVisitante().getId());
    }
}
