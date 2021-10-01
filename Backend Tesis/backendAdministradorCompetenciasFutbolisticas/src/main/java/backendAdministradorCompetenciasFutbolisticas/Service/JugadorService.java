package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.EstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorClub;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Repository.EstadoJugadorRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JugadorService {

    @Autowired
    JugadorRepository jugadorRepository;

    @Autowired
    EstadoJugadorRepository estadoJugadorRepository;

    @Autowired
    JugadorClubService jugadorClubService;

    public boolean guardarNuevoJugador(Jugador nuevoJugador){
        EstadoJugador estadoActivo = estadoJugadorRepository.findByEstadoJugador(NombreEstadoJugador.ACTIVO);
        nuevoJugador.setEstadoJugador(estadoActivo);
        return jugadorRepository.save(nuevoJugador).getId() != null;
    }

    public Jugador guardarJugador(Jugador jugador){
        return jugadorRepository.save(jugador);
    }

    public Jugador save(Jugador jugador){
        return jugadorRepository.save(jugador);
    }

    public void eliminar(Long id){
        jugadorRepository.deleteById(id);
    }

    public List<Jugador> getListadoJugadoresOrdenadoPorApellido(){
        return jugadorRepository.findByOrderByApellidosAsc();
    }

    public List<Jugador> getListadoJugadoresDeUnClub(Long idClub){
        return jugadorRepository.findByClubActual_Id(idClub);
    }

    public Optional<Jugador> getJugadorPorId(Long id){
        return jugadorRepository.findById(id);
    }

    public boolean existePorId(Long id){
        return jugadorRepository.existsById(id);
    }

    public  boolean existeJugadorPorDocumento(String documento){
        return jugadorRepository.existsByDocumento(documento);
    }

    public boolean cambiarEstadoAInactivo(Jugador jugador){
        EstadoJugador estadoInactivo = estadoJugadorRepository.findByEstadoJugador(NombreEstadoJugador.INACTIVO);
        jugador.setEstadoJugador(estadoInactivo);
        jugadorRepository.save(jugador);
        return  jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.INACTIVO);
    }

    public boolean cambiarEstadoAActivo(Jugador jugador){
        EstadoJugador estadoActivo = estadoJugadorRepository.findByEstadoJugador(NombreEstadoJugador.ACTIVO);
        jugador.setEstadoJugador(estadoActivo);
        jugadorRepository.save(jugador);
        return  jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.ACTIVO);
    }

    public boolean cambiarEstadoARetirado(Jugador jugador){
        EstadoJugador estadoRetirado = estadoJugadorRepository.findByEstadoJugador(NombreEstadoJugador.RETIRADO);
        jugador.setEstadoJugador(estadoRetirado);
        jugadorRepository.save(jugador);
        return  jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.RETIRADO);
    }

    /*
    *   Metodo que valida que la fecha de realizacion de una transferencia (cambio de club de un jugador)
    *   sea mayor a la fecha de su ultima transferencia
     */
    public boolean validarFechaCambioDeClub(LocalDate fechaCambioClub, Jugador jugador){
        List<JugadorClub> historial = jugadorClubService.historialJugador(jugador.getId());
        JugadorClub ultimoPase = historial.get(historial.size() - 1);
        return fechaCambioClub.isAfter(ultimoPase.getFecha());
    }

    /*
    *   Metodo que valida que al realizar una transferencia, el club sea diferente del club
    *   en que se encuentra actualmente el jugador
     */
    public boolean validarClubNoIgualesAlCambiarDeClub(Club clubACambiar, Jugador jugador){
        if(clubACambiar.getId().equals(jugador.getClubActual().getId())){
            return false;
        }
        return true;
    }


    /*
    *   Metodo que retorna la ultima transferencia realizada por un jugador
     */
    public JugadorClub getUltimaTransferencia(Jugador jugador){
        List<JugadorClub> historial = jugadorClubService.historialJugador(jugador.getId());
        return historial.get(historial.size() - 1 );
    }

    /*
    *   Metodo que retorna el club en que el jugador estuvo en una cierta fecha
     */
    public Club getClubEnFecha(Jugador jugador, LocalDate fecha){
        List<JugadorClub> historial = jugadorClubService.historialJugador(jugador.getId());
        List<JugadorClub> historialHastaFecha =
                historial.stream()
                        .filter(pase -> pase.getFecha().isBefore(fecha) || pase.getFecha().equals(fecha))
                        .collect(Collectors.toList());
        if(historialHastaFecha.isEmpty()){
            return null;
        }
        Club club = historialHastaFecha.get(historialHastaFecha.size() - 1).getClub();
        return club;
    }


}
