package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.EstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.EstadoJugadorRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.JugadorRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.PaseJugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public Integer cantidadJugadores(){
        return jugadorRepository.countJugadorBy();
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
            throw new ResourceNotFoundException("El jugador con ID: " + id + " no existe");
        }
        return jugadorOptional.get();
    }

    public Jugador getJugadorPorDni(String dni){
        Optional<Jugador> jugadorOptional = jugadorRepository.findByDocumento(dni);
        if(!jugadorOptional.isPresent()){
            throw new ResourceNotFoundException("El jugador con DNI: " + dni + " no existe");
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
        Metodo que valida que la fecha de realizacion de una transferencia (cambio de club de un jugador)
        sea mayor a la fecha de su ultima transferencia
    */
    public void validarFechaCambioDeClub(LocalDate fechaCambioClub, Jugador jugador){
        Pase ultimoPase = getUltimaTransferencia(jugador.getId());

        if(ultimoPase!=null && ultimoPase.getFechaHasta() != null && fechaCambioClub.isBefore(ultimoPase.getFechaHasta())){
            throw new BadRequestException("La fecha ingresada no debe ser menor a la fecha hasta de su ultimo cambio de club: "+ ultimoPase.getFechaHasta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        if(ultimoPase!=null && fechaCambioClub.isBefore(ultimoPase.getFechaDesde())){
            throw new BadRequestException("La fecha ingresada no debe ser menor a la fecha desde de su ultimo cambio de club: "+ ultimoPase.getFechaDesde().format(DateTimeFormatter.ofPattern("dd/mm/yyy")));
        }
    }

    /*
         Metodo que valida que al realizar una transferencia, el club sea diferente del club
         en que se encuentra actualmente el jugador
    */
    public void validarClubNoIgualesAlCambiarDeClub(Club clubACambiar, Jugador jugador){
        if(jugador.getClubActual()!= null && clubACambiar.getId().equals(jugador.getClubActual().getId())){
            throw new BadRequestException("El Club ingresado es el Club actual del jugador");
        }
    }

    /*
        Metodo que retorna la primer transferencia realizada, es decir su primera inscripcion
    */
    public Pase getPrimerTransferencia(Long id){
        Pase primerPase = paseJugadorRepository.findFirstByJugador_Id(id);
        return primerPase;
    }


    /*
    *   Metodo que retorna la ultima transferencia fecha hasta is null realizada por un jugador
     */
    public Pase getUltimaTransferenciaFechaHastaIsNUll(Long id){
        Pase ultimoPase = paseJugadorRepository.findByJugador_IdAndFechaHastaIsNull(id);
        return ultimoPase;
    }

    public Pase getUltimaTransferencia(Long id){
        List<Pase> pases =  paseJugadorRepository.findByJugador_IdOrderByFechaDesdeAsc(id);
        return pases.isEmpty()? null :  pases.get(pases.size()-1);
    }

    /*
     *   Metodo que retorna el club actual de un jugador, pase cuya fecha hasta es null
     */
    public Club getClubActualJugador(Long id){
        Pase clubActual = paseJugadorRepository.findByJugador_IdAndFechaHastaIsNull(id);
        return clubActual.getClub();
    }

    /*
    *   Metodo que retorna el club en que el jugador estuvo en una cierta fecha
    *   TODO: AUN NO FUNCIONA
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
