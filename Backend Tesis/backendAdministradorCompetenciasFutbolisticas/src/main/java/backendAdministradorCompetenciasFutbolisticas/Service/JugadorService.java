package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.EstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.RecursoNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.EstadoJugadorRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.JugadorRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.PaseJugadorRepository;
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
    PaseJugadorRepository paseJugadorRepository;

    @Autowired
    PaseJugadorService paseJugadorService;

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

    public Jugador getJugador(Long id){
        Optional<Jugador> jugadorOptional = jugadorRepository.findById(id);
        if(!jugadorOptional.isPresent()){
            throw new RecursoNotFoundException("El jugador con ID: " + id + " no existe");
        }
        return jugadorOptional.get();
    }

    public Jugador getJugadorPorDni(String dni){
        Optional<Jugador> jugadorOptional = jugadorRepository.findByDocumento(dni);
        if(!jugadorOptional.isPresent()){
            throw new RecursoNotFoundException("El jugador con DNI: " + dni + " no existe");
        }
        return jugadorOptional.get();
    }

    public boolean existePorId(Long id){
        return jugadorRepository.existsById(id);
    }

    public  boolean existeJugadorPorDocumento(String documento){
        return jugadorRepository.existsByDocumento(documento);
    }

    public boolean cambiarEstadoAInactivo(Jugador jugador){
        if(jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.ACTIVO)){
            throw new BadRequestException("El jugador ya se encuentra Inactivo");
        }
        EstadoJugador estadoInactivo = estadoJugadorRepository.findByEstadoJugador(NombreEstadoJugador.INACTIVO);
        jugador.setEstadoJugador(estadoInactivo);
        jugadorRepository.save(jugador);
        return  jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.INACTIVO);
    }

    public boolean cambiarEstadoAActivo(Jugador jugador){
        if(jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.ACTIVO)){
            throw new BadRequestException("El jugador ya se encuentra Activo");
        }
        EstadoJugador estadoActivo = estadoJugadorRepository.findByEstadoJugador(NombreEstadoJugador.ACTIVO);
        jugador.setEstadoJugador(estadoActivo);
        jugadorRepository.save(jugador);
        return  jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.ACTIVO);
    }

    public boolean cambiarEstadoARetirado(Jugador jugador){
        if(jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.RETIRADO)){
            throw new BadRequestException("El jugador ya se encuentra Retirado");
        }
        EstadoJugador estadoRetirado = estadoJugadorRepository.findByEstadoJugador(NombreEstadoJugador.RETIRADO);
        jugador.setEstadoJugador(estadoRetirado);
        jugadorRepository.save(jugador);
        return  jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.RETIRADO);
    }

    /*
    *   Metodo que valida que la fecha de realizacion de una transferencia (cambio de club de un jugador)
    *   sea mayor a la fecha de su ultima transferencia
     */
    public void validarFechaCambioDeClub(LocalDate fechaCambioClub, Jugador jugador){
        Pase ultimoPase = paseJugadorRepository.findByJugador_IdAndFechaHastaIsNull(jugador.getId());
       // return fechaCambioClub.isAfter(ultimoPase.getFechaDesde());
        if(!fechaCambioClub.isAfter(ultimoPase.getFechaDesde())){
            throw new BadRequestException("La fecha ingresada no debe ser menor a la fecha de su ultimo cambio de club");
        }
    }

    /*
    *   Metodo que valida que al realizar una transferencia, el club sea diferente del club
    *   en que se encuentra actualmente el jugador
     */
    public void validarClubNoIgualesAlCambiarDeClub(Club clubACambiar, Jugador jugador){
        if(clubACambiar.getId().equals(jugador.getClubActual().getId())){
            throw new BadRequestException("El Club ingresado es el Club actual del jugador");
        }
    }


    /*
    *   Metodo que retorna la ultima transferencia realizada por un jugador
     */
    public Pase getUltimaTransferencia(Long id){
        Pase ultimoPase = paseJugadorRepository.findByJugador_IdAndFechaHastaIsNull(id);
        return ultimoPase;
    }


    public Club getClubActualJugador(Long id){
        Pase clubActual = paseJugadorRepository.findByJugador_IdAndFechaHastaIsNull(id);
        return clubActual.getClub();
    }

    /*
    *   Metodo que retorna el club en que el jugador estuvo en una cierta fecha
     */
    public Club getClubEnFecha(Jugador jugador, LocalDate fecha){
        List<Pase> historial = paseJugadorService.historialJugador(jugador.getId());
        System.out.println("historial cantidad " + historial.size());
        List<Pase> historialHastaFecha =
                historial.stream()
                        .filter(pase -> fecha.isAfter(pase.getFechaDesde()) || pase.getFechaDesde().isEqual(fecha)
                                &&  (pase.getFechaHasta().isBefore(fecha) || pase.getFechaHasta() == null))
                        .collect(Collectors.toList());


        if(historialHastaFecha.isEmpty()){
            return null;
        }
        System.out.println(historialHastaFecha.size());
        Club club = historialHastaFecha.get(historialHastaFecha.size() - 1).getClub();
        return club;
    }


}
