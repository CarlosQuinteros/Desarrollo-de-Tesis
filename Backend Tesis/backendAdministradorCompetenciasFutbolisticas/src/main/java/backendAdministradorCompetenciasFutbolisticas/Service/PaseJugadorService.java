package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.JugadorRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.PaseJugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaseJugadorService {

    @Autowired
    PaseJugadorRepository paseJugadorRepository;

    @Autowired
    JugadorRepository jugadorRepository;

    public boolean guardar(Pase pase){
        return paseJugadorRepository.save(pase).getId() != null;
    }

    public List<Pase> getListadoPases(){
        return paseJugadorRepository.findAll();
    }

    public boolean existeHistorialPorClub(Long id){
        return paseJugadorRepository.existsByClub_Id(id);
    }

    //predicado con estado para filtrar distinct con propiedad
    public static <T> Predicate<T> distinctByKey(
            Function< ? super T, ?> keyExtractor){
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t),Boolean.TRUE) == null;
    }

    public List<Pase> historialJugador(Long id){
        return paseJugadorRepository.findByJugador_IdOrderByFechaDesdeAsc(id);
    }

    public List<Pase> historialExJugadoresPorIdClub(Long idClub){
        return paseJugadorRepository.findDistinctByClub_IdAndJugador_ClubActual_IdNot(idClub,idClub);
    }

    public List<Jugador> jugadoresActualesPorClub(Long idClub){
        List<Jugador> jugadores = paseJugadorRepository.findByClub_IdAndFechaHastaIsNull(idClub).stream().map(Pase::getJugador).collect(Collectors.toList());
        return jugadores;
    }

    public List<Jugador> exJugadoresPorClub(Long idClub){

        //List<Jugador> jugadores = paseJugadorRepository.findByClub_IdAndFechaHastaIsNotNull(idClub).stream().map(Pase::getJugador).collect(Collectors.toList());
        List<Jugador> jugadores = paseJugadorRepository.findByClub_IdAndFechaHastaIsNotNull(idClub).stream().filter(distinctByKey(p -> p.getJugador().getId())).map(Pase::getJugador).collect(Collectors.toList());

        return jugadores;
    }

    public void eliminarHistorialDeUnJugador(Jugador jugador){
        List<Pase> historial = historialJugador(jugador.getId());
        paseJugadorRepository.deleteAll(historial);
    }

    public Integer cantidadPases(){
        //return paseJugadorRepository.countByMotivoNot("Inscripcion");
        return paseJugadorRepository.countPaseBy();
    }

    public Pase getPaseById(Long idPase){
        return paseJugadorRepository.findById(idPase)
                .orElseThrow(() -> new ResourceNotFoundException("EL pase con ID: " + idPase + " no existe"));
    }

    public void eliminarPase(Long idPase){
        Pase pase = paseJugadorRepository.getById(idPase);
        Jugador  jugador = pase.getJugador();
        if(pase.getFechaHasta() == null){
            jugador.setClubActual(null);
            paseJugadorRepository.delete(pase);
            jugadorRepository.save(jugador);
        }
        else{
            paseJugadorRepository.delete(pase);
        }
    }



}
