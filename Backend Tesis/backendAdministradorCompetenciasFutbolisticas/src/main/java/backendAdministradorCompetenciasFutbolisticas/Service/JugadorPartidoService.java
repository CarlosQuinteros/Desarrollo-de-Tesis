package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Enums.PosicionJugador;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoRolJugador;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.JugadorPartidoRepository;
import ch.qos.logback.classic.jul.JULHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JugadorPartidoService {

    @Autowired
    private JugadorPartidoRepository jugadorPartidoRepository;

    @Autowired
    private AnotacionService anotacionService;

    @Autowired
    private SustitucionService sustitucionService;

    public List<PosicionJugador> getListadoPosicionJugador(){
        return Arrays.stream(PosicionJugador.values()).collect(Collectors.toList());
    }

    public List<String> getListadoStringPosiciones(){
        return getListadoPosicionJugador()
                .stream()
                .map(PosicionJugador::name)
                .collect(Collectors.toList());
    }

    public PosicionJugador getPosicionJugadorPorNombre(String nombre){
        return getListadoPosicionJugador().stream()
                .filter(posicionJugador -> posicionJugador.name().equals(nombre.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("No existe la posicion: " + nombre));
    }

    public JugadorPartido guardarParticipacionJugadorTitular(JugadorPartido jugadorPartido){
        if(existeParticipacionPorIdPartidoYJugador(jugadorPartido.getPartido().getId(), jugadorPartido.getJugador().getId())){
            throw new BadRequestException("El jugador solo puede tener una participacion en un partido");
        }
        if(jugadorPartido.getPosicion().equals(PosicionJugador.ARQ) && existeArqueroEnTitularesPorPartidoYClub(jugadorPartido.getPartido().getId(), jugadorPartido.getClub().getId())){
            throw new BadRequestException("El listado de titulares del club " + jugadorPartido.getClub().getNombreClub() + " solo puede tener un arquero");
        }
        if(cantidadParticipacionesPorPartidoYClubYRol(jugadorPartido.getPartido().getId(), jugadorPartido.getClub().getId(), TipoRolJugador.TITULAR) == 11){
            throw new BadRequestException("El club " + jugadorPartido.getClub().getNombreClub() + " puede tener hasta 11 titualres");
        }
        jugadorPartido.setRol(TipoRolJugador.TITULAR);
        return jugadorPartidoRepository.save(jugadorPartido);

    }

    public JugadorPartido guardarParticipacionJugadorSuplente(JugadorPartido jugadorPartido){
        if(existeParticipacionPorIdPartidoYJugador(jugadorPartido.getPartido().getId(), jugadorPartido.getJugador().getId())){
            throw new BadRequestException("El jugador solo puede tener una participacion en un partido");
        }
        jugadorPartido.setRol(TipoRolJugador.SUPLENTE);
        return jugadorPartidoRepository.save(jugadorPartido);
    }

    public JugadorPartido guardarParticipacionJugador(JugadorPartido jugadorPartido){
        return jugadorPartidoRepository.save(jugadorPartido);
    }

    public JugadorPartido getJugadorPartidoById(Long id){
        return jugadorPartidoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No existe la participacion con Id: " + id));
    }

    //TODO: No se puede eliminar si el jugador tiene tarjetas
    public void eliminarParticipacionJugador(Long idJugadorPartido){
        JugadorPartido participacionJugador = getJugadorPartidoById(idJugadorPartido);
        if(anotacionService.existeAnotacionEnPartidoDeJugador(participacionJugador.getPartido().getId(), participacionJugador.getJugador().getId())){
            throw new BadRequestException("El jugador anoto un gol y no se puede eliminar su participacion");
        }
        if(sustitucionService.existeSustitucionPorPartidoYClubYJugadorSale(participacionJugador.getPartido().getId(), participacionJugador.getClub().getId(),participacionJugador.getJugador().getId())){
            throw new BadRequestException("El jugador salio en una sustitucion y no se puede eliminar su participacion");
        }
        if(sustitucionService.existeSustitucionPorPartidoYClubYJugadorEntra(participacionJugador.getPartido().getId(), participacionJugador.getClub().getId(),participacionJugador.getJugador().getId())){
            throw new BadRequestException("El jugador entro en una sustitucion y no se puede eliminar su participacion");
        }
        jugadorPartidoRepository.deleteById(idJugadorPartido);
    }

    public boolean existeArqueroEnTitularesPorPartidoYClub(Long idPartido,Long idClub){
        return jugadorPartidoRepository.existsByPartido_IdAndClub_IdAndRolAndPosicion(idPartido, idClub, TipoRolJugador.TITULAR, PosicionJugador.ARQ);
    }

    public boolean existeParticipacionPorIdPartidoYJugador(Long idPartido, Long idJugador){
        return jugadorPartidoRepository.existsByPartido_IdAndJugador_Id(idPartido, idJugador);
    }

    public Integer cantidadParticipacionesPorPartidoYClubYRol(Long idPartido, Long idClub, TipoRolJugador rolJugador){
        return jugadorPartidoRepository.countByPartido_IdAndClub_IdAndRol(idPartido, idClub, rolJugador);
    }

    public List<JugadorPartido> getListadoTotalDeJugadoresPorPartidoYClub(Partido partido, Club club){
        List<JugadorPartido> jugadores = jugadorPartidoRepository.findByPartido_IdAndClub_Id(partido.getId(), club.getId());
        return jugadores;
    }

    public List<JugadorPartido> getListadoJugadoresTitularesClubLocal(Partido partido){
        List<JugadorPartido> titulares = jugadorPartidoRepository
                .findByPartido_IdAndClub_IdAndRol(partido.getId(), partido.getClubLocal().getId(),TipoRolJugador.TITULAR);
        return titulares;
    }

    public List<JugadorPartido> getListadoJugadoresSuplentesClubLocal(Partido partido){
        List<JugadorPartido> suplentes = jugadorPartidoRepository
                .findByPartido_IdAndClub_IdAndRol(partido.getId(), partido.getClubLocal().getId(),TipoRolJugador.SUPLENTE);
        return suplentes;
    }

    public List<JugadorPartido> getListadoJugadoresTitularesClubVisitante(Partido partido){
        List<JugadorPartido> titulares = jugadorPartidoRepository
                .findByPartido_IdAndClub_IdAndRol(partido.getId(), partido.getClubVisitante().getId(),TipoRolJugador.TITULAR);
        return titulares;
    }

    public List<JugadorPartido> getListadoJugadoresSuplentesClubVisitante(Partido partido){
        List<JugadorPartido> suplentes = jugadorPartidoRepository
                .findByPartido_IdAndClub_IdAndRol(partido.getId(), partido.getClubVisitante().getId(),TipoRolJugador.SUPLENTE);
        return suplentes;
    }

    public List<JugadorPartido> getParticipacionesDeJugador(Jugador jugador){
        List<JugadorPartido> participaciones = jugadorPartidoRepository.findByJugador_Id(jugador.getId());
        return participaciones;
    }

    public boolean jugadorFormaParteDelEquipoEnPartido(Long idPartido, Long idClub, Long idJugador){
        return jugadorPartidoRepository.existsByPartido_IdAndClub_IdAndJugador_Id(idPartido,idClub,idJugador);
    }

    public boolean jugadorFormaParteDeTitulares(Long idPartido, Long idClub, Long idJugador){
        return jugadorPartidoRepository.existsByPartido_IdAndClub_IdAndJugador_IdAndRol(idPartido,idClub, idJugador,TipoRolJugador.TITULAR);
    }

    public boolean jugadorFormaParteDeSuplentes(Long idPartido, Long idClub, Long idJugador){
        return jugadorPartidoRepository.existsByPartido_IdAndClub_IdAndJugador_IdAndRol(idPartido,idClub, idJugador,TipoRolJugador.SUPLENTE);
    }

    public boolean existeReferenciasConPartido(Long idPartido){
        return jugadorPartidoRepository.existsByPartido_Id(idPartido);
    }






}
