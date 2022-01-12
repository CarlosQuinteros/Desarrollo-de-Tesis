package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.RecursoNotFoundException;
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
                .orElseThrow(() -> new RecursoNotFoundException("EL pase con ID: " + idPase + " no existe"));
    }

    public void eliminarPase(Long idPase){
        Pase pase = paseJugadorRepository.getById(idPase);
        Jugador  jugador = pase.getJugador();
        if(paseJugadorRepository.countPaseByJugador_Id(jugador.getId()) == 1){
            throw new BadRequestException("El jugador tiene solo un unico historial y no puede eliminarse");
        }
        List<Pase> historial = historialJugador(jugador.getId());

        if(paseJugadorRepository.existsByIdAndFechaHastaIsNull(idPase)){
            historial.forEach(pase1 -> System.out.println(pase1.getId()));
            Pase paseAnterior= historial.get(historial.size()-2);
            System.out.println("\nel que deberia ser ultimo pase: " + paseAnterior.getId());
            historial.remove(pase);
            System.out.println("luego de eliminar el pase del historial obtenido");
            System.out.println("el que deberia ser el ultimo pase: "+ historial.get(historial.size()-1).getId());
        }

        System.out.println("como no tiene null en fecha desde, no obtengo el ultimo pase, solo elimino");
        historial.remove(pase);
        historial.forEach(pase1 -> System.out.println(pase1.getId()));



    }



}
