package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.CambioDeClubDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Dtos.NuevoJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InternalServerErrorException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.PaseJugadorService;
import backendAdministradorCompetenciasFutbolisticas.Service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jugadores")
@CrossOrigin("*")
public class JugadorController {


    @Autowired
    JugadorService jugadorService;

    @Autowired
    ClubService clubService;

    @Autowired
    PaseJugadorService paseJugadorService;

    @Autowired
    UsuarioService usuarioService;


    /*
    *   Metodo que permite crear un jugador, no debe existir el mismo documento
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearJugador(@Valid @RequestBody NuevoJugadorDto nuevoJugadorDto , BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("Campos mal ingresados"),HttpStatus.NOT_FOUND);
        }
        if(jugadorService.existeJugadorPorDocumento(nuevoJugadorDto.getDocumento())){
            return new ResponseEntity<>(new Mensaje("Ya existe un jugador con el DNI ingresado"), HttpStatus.BAD_REQUEST);
        }
        Optional<Club> clubOptional = clubService.getById(nuevoJugadorDto.getIdClub());
        if(!clubOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El club no existe"), HttpStatus.BAD_REQUEST);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        Club club = clubOptional.get();
        Jugador nuevoJugador = new Jugador(nuevoJugadorDto.getNombres(), nuevoJugadorDto.getApellidos(), nuevoJugadorDto.getDocumento(), nuevoJugadorDto.getFechaNacimiento() );
        nuevoJugador.setClubActual(club);
        Pase historiaClub = new Pase(LocalDate.now(),null,nuevoJugador,club,"Inscripcion",usuario);
        jugadorService.guardarNuevoJugador(nuevoJugador);
        paseJugadorService.guardar(historiaClub);
        try {
            boolean resultado = jugadorService.guardarNuevoJugador(nuevoJugador);
            if(resultado){
                return new ResponseEntity<>(new Mensaje("Jugador guardado correctamente"), HttpStatus.CREATED);
            }
            throw new InternalServerErrorException("Fallo la operación. Jugador no guardado correctamente");
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operación. Jugador no guardado correctamente");
        }

    }

    /*
    *   Metodo que retorna el listado de jugadores ordenado por apellido
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/listado")
    public ResponseEntity<List<Jugador>> listadoJugadores(){
        List<Jugador> listado = jugadorService.getListadoJugadoresOrdenadoPorApellido();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }


    /*
    *   Metodo que retorna el detalle de un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("detalle/{id}")
    public ResponseEntity<Jugador> detalleJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);
        return new ResponseEntity<>(jugador, HttpStatus.OK);
    }


    /*
    *   Metodod que retorna la edad del jugador en cierta fecha.
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/edadEnFecha/{id}")
    public ResponseEntity<Integer> edadJugadorEnCiertaFecha(@PathVariable ("id") Long id, @RequestBody LocalDate fecha){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);
        return new ResponseEntity<>(jugador.getEdadEnFecha(fecha), HttpStatus.OK);
    }

    /*
    *   Metodo que permite dar de baja un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/bajaJugador/{id}")
    public ResponseEntity<?> darDeBajaJugador(@PathVariable ("id") Long id){
        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);

        //el servicio genera badRequest exception si el jugador ya esta inactivo
        boolean resultado = jugadorService.cambiarEstadoAInactivo(jugador);
        if(resultado){
            return new ResponseEntity<>(new Mensaje("Jugador dado de Baja correctamente"),HttpStatus.OK);
        }
        throw new InternalServerErrorException("No se dio de Baja al jugador");    }

    /*
    *   Metodo que permite dar de alta un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/altaJugador/{id}")
    public ResponseEntity<?> darDeAltaJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);

        //el servicio genera badRequest exception si el jugador ya esta activo
        boolean resultado = jugadorService.cambiarEstadoAActivo(jugador);
        if(resultado){
            return new ResponseEntity<>(new Mensaje("Jugador dado de Alta correctamente"),HttpStatus.OK);
        }
        throw new InternalServerErrorException("No se dio de Alta al jugador");
    }

    /*
    *   Metodo que permite dar el retiro a un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/jugadorRetirado/{id}")
    public ResponseEntity<?> darRetiroJugador(@PathVariable ("id") Long id) {
        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);

        //el servicio genera badRequest exception si el jugador ya esta activo
        boolean resultado = jugadorService.cambiarEstadoARetirado(jugador);
        if (resultado) {
            return new ResponseEntity<>(new Mensaje("Jugador retirado correctamente"), HttpStatus.OK);
        }
        throw new InternalServerErrorException("No se cambio el estado del jugador");
    }


    /*
    *  Metodo para elimninar un jugador y su historial. Solo en ambito de desarrollo
    */

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);

        paseJugadorService.eliminarHistorialDeUnJugador(jugador);
        jugadorService.eliminar(id);
        return new ResponseEntity<>(new Mensaje("Jugador eliminado correctamente"),HttpStatus.OK);
    }


    /*
    *   Metodo que permite el cambio de club de un jugador.
    *   La fecha ingresada debe ser mayor a la fecha de su ultimo cambio de club.
    *   El club ingresado debe ser distinto de tu club actual.
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PostMapping("/cambioClub")
    public ResponseEntity<?> cambioDeClubJugador(@Valid @RequestBody CambioDeClubDto cambioDeClubDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            //return new ResponseEntity<>(new Mensaje("Campos mal ingresados"),HttpStatus.NOT_FOUND);
            throw new InvalidDataException(bindingResult);
        }

        //el servicio genera notFound exception si no encuentra el jugador o el club
        Jugador jugador = jugadorService.getJugador(cambioDeClubDto.getIdJugador());
        Club club = clubService.getClub(cambioDeClubDto.getIdClub());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);

       //valida fecha ingresada de cambio de club, validar fecha genera badrequestException si no es valida
       jugadorService.validarFechaCambioDeClub(cambioDeClubDto.getFecha(), jugador);

       //valida que el club ingresado no sea el el club actual del jugador, genera badrequest si es el caso
       jugadorService.validarClubNoIgualesAlCambiarDeClub(club,jugador);
        try {
            Pase ultimoPase = jugadorService.getUltimaTransferencia(jugador.getId());
            ultimoPase.setFechaHasta(cambioDeClubDto.getFecha());
            paseJugadorService.guardar(ultimoPase);
            Pase nuevaHistoriaClub = new Pase(cambioDeClubDto.getFecha(), null, jugador, club, cambioDeClubDto.getMotivo(), usuario);
            Jugador jugadorActualizado = new Jugador();
            if(paseJugadorService.guardar(nuevaHistoriaClub)){
                jugador.setClubActual(club);
                jugadorActualizado = jugadorService.save(jugador);
            }
            return new ResponseEntity<>(new Mensaje("Se cambio el club del jugador correctamente", jugadorActualizado), HttpStatus.OK);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. No se registró el cambio de club");
        }

    }

    /*
    *   Metodo que retorna el historial de clubes de un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("historialClubes/{id}")
    public ResponseEntity<List<Pase>> historialClubesDeJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);
        List<Pase> historial = paseJugadorService.historialJugador(id);
        return new ResponseEntity<>(historial,HttpStatus.OK);
    }


    /*
    *   Metodo que retorna los jugadores de un cierto club.
    *   Se tiene en cuenta el club actual del jugador.
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/jugadoresPorClub/{idClub}")
    public ResponseEntity<List<Jugador>> listaJugadoresDeUnClub(@PathVariable ("idClub") Long idClub){
        List<Jugador> listado = jugadorService.getListadoJugadoresDeUnClub(idClub);
        return new ResponseEntity<>(listado,HttpStatus.OK);
    }

    @GetMapping("ultimoPaseJugador/{id}")
    public ResponseEntity<?> ultimaTransferenciaJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador =jugadorService.getJugador(id);

        Pase ultimoPase = jugadorService.getUltimaTransferencia(id);
        List<Pase> historial = paseJugadorService.historialJugador(id);
        return new ResponseEntity<>(ultimoPase, HttpStatus.OK);
    }

    @GetMapping("clubActual/{id}")
    public ResponseEntity<Club> clubActualDeJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador =jugadorService.getJugador(id);

        Club club = jugadorService.getClubActualJugador(id);
        return new ResponseEntity<>(club, HttpStatus.OK);
    }

    //TODO: NO FUNCIONA CORRECTAMENTE
    @GetMapping("ClubEnFecha/{id}")
    public ResponseEntity<?> clubEnCiertaFecha(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador =jugadorService.getJugador(id);

        //prueba con "2022-03-30"
        LocalDate fecha = LocalDate.of(2021, Month.OCTOBER, 4);
        Club club = jugadorService.getClubEnFecha(jugador, fecha);
        if(club == null){
            //return new ResponseEntity<>(new Mensaje("No tiene clubes"),HttpStatus.BAD_REQUEST);
            throw new BadRequestException("El jugador no tiene historial de clubes hasta la fecha indicada");
        }
        return new ResponseEntity<>(club,HttpStatus.OK);
    }



}
