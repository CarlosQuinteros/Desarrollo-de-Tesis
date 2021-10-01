package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.CambioDeClubDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Dtos.NuevoJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorClub;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.UsuarioPrincipal;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.JugadorClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    JugadorClubService jugadorClubService;

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
        JugadorClub historiaClub = new JugadorClub(LocalDate.now(),nuevoJugador,club,"Inscripción", usuario);
        jugadorService.guardarNuevoJugador(nuevoJugador);
        jugadorClubService.guardar(historiaClub);
        try {
            boolean resultado = jugadorService.guardarNuevoJugador(nuevoJugador);
            if(resultado){
                return new ResponseEntity<>(new Mensaje("Jugador guardado correctamente"), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(new Mensaje("Jugador no guardado correctamente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operación. Jugador no guardado correctamente"), HttpStatus.INTERNAL_SERVER_ERROR);

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
    public ResponseEntity<?> detalleJugador(@PathVariable ("id") Long id){
        Optional<Jugador> jugadorOptional = jugadorService.getJugadorPorId(id);
        if(!jugadorOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El jugador no existe"),HttpStatus.NOT_FOUND);
        }
        Jugador jugador = jugadorOptional.get();
        return new ResponseEntity<>(jugador, HttpStatus.OK);
    }




    /*
    *   Metodod que retorna la edad del jugador en cierta fecha.
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/edadEnFecha/{id}")
    public ResponseEntity<Integer> edadJugadorEnCiertaFecha(@PathVariable ("id") Long id, @RequestBody LocalDate fecha){
        Jugador jugador = jugadorService.getJugadorPorId(id).get();
        return new ResponseEntity<>(jugador.getEdadEnFecha(fecha), HttpStatus.OK);
    }

    /*
    *   Metodo que permite dar de baja un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/bajaJugador/{id}")
    public ResponseEntity<?> darDeBajaJugador(@PathVariable ("id") Long id){
        Optional<Jugador> jugadorOptional = jugadorService.getJugadorPorId(id);
        if(!jugadorOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El jugador no existe"), HttpStatus.NOT_FOUND);
        }
        Jugador jugador = jugadorOptional.get();
        if(jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.INACTIVO)){
            return new ResponseEntity<>(new Mensaje("El jugador ya se encuentra Inactivo"),HttpStatus.BAD_REQUEST);
        }
        boolean resultado = jugadorService.cambiarEstadoAInactivo(jugador);
        if(resultado){
            return new ResponseEntity<>(new Mensaje("Jugador dado de Baja correctamente"),HttpStatus.OK);
        }
        return  new ResponseEntity<>(new Mensaje("No se dio de Baja al jugador"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
    *   Metodo que permite dar de alta un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/altaJugador/{id}")
    public ResponseEntity<?> darDeAltaJugador(@PathVariable ("id") Long id){
        Optional<Jugador> jugadorOptional = jugadorService.getJugadorPorId(id);
        if(!jugadorOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El jugador no existe"), HttpStatus.NOT_FOUND);
        }
        Jugador jugador = jugadorOptional.get();
        if(jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.ACTIVO)){
            return new ResponseEntity<>(new Mensaje("El jugador ya se encuentra Activo"),HttpStatus.BAD_REQUEST);
        }
        boolean resultado = jugadorService.cambiarEstadoAActivo(jugador);
        if(resultado){
            return new ResponseEntity<>(new Mensaje("Jugador dado de Alta correctamente"),HttpStatus.OK);
        }
        return  new ResponseEntity<>(new Mensaje("No se dio de Alta al jugador"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
    *   Metodo que permite dar el retiro a un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/jugadorRetirado/{id}")
    public ResponseEntity<?> darRetiroJugador(@PathVariable ("id") Long id) {
        Optional<Jugador> jugadorOptional = jugadorService.getJugadorPorId(id);
        if (!jugadorOptional.isPresent()) {
            return new ResponseEntity<>(new Mensaje("El jugador no existe"), HttpStatus.NOT_FOUND);
        }
        Jugador jugador = jugadorOptional.get();
        if (jugador.getEstadoJugador().getNombreEstado().equals(NombreEstadoJugador.RETIRADO)) {
            return new ResponseEntity<>(new Mensaje("El jugador ya se encuentra Retirado"), HttpStatus.BAD_REQUEST);
        }
        boolean resultado = jugadorService.cambiarEstadoARetirado(jugador);
        if (resultado) {
            return new ResponseEntity<>(new Mensaje("Jugador retirado correctamente"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Mensaje("No se cambio el estado del jugador"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /*
    *  Metodo para elimninar un jugador y su historial. Solo en ambito de desarrollo
    */

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarJugador(@PathVariable ("id") Long id){
        if(!jugadorService.existePorId(id)){
            return new ResponseEntity<>(new Mensaje("El Jugador no existe"), HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<?> cambioDeClubJugador(@Valid @RequestBody CambioDeClubDto cambioDeClubDto){
        Optional<Jugador> jugadorOptional = jugadorService.getJugadorPorId(cambioDeClubDto.getIdJugador());
        Optional<Club> clubOptional = clubService.getById(cambioDeClubDto.getIdClub());
        if(!jugadorOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El Jugador no existe"), HttpStatus.NOT_FOUND);
        }
        if(!clubOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El Club no existe"), HttpStatus.NOT_FOUND);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        Jugador jugador = jugadorOptional.get();
        Club club = clubOptional.get();
        if(!jugadorService.validarFechaCambioDeClub(cambioDeClubDto.getFecha(), jugador)){
            return new ResponseEntity<>(new Mensaje("La fecha ingresada no debe ser menor a la fecha de su ultimo cambio de club"),HttpStatus.BAD_REQUEST);
        }
        if(!jugadorService.validarClubNoIgualesAlCambiarDeClub(club, jugador)){
            return new ResponseEntity<>(new Mensaje("El Club actual es el mismo club ingresado"),HttpStatus.BAD_REQUEST);
        }
        try {
            JugadorClub nuevaHistoriaClub = new JugadorClub(cambioDeClubDto.getFecha(), jugador, club, cambioDeClubDto.getMotivo(), usuario);
            Jugador jugadorActualizado = new Jugador();
            if(jugadorClubService.guardar(nuevaHistoriaClub)){
                jugador.setClubActual(club);
                jugadorActualizado = jugadorService.save(jugador);
            }
            return new ResponseEntity<>(new Mensaje("Se cambio el club del jugador correctamente", jugadorActualizado), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. No se registró el cambio de Club"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
    *   Metodo que retorna el historial de clubes de un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("historialClubes/{id}")
    public ResponseEntity<List<JugadorClub>> historialClubesDeJugador(@PathVariable ("id") Long id){
        Optional<Jugador> jugadorOptional = jugadorService.getJugadorPorId(id);
        if(!jugadorOptional.isPresent()){
            return new ResponseEntity(new Mensaje("El Jugador no existe"), HttpStatus.NOT_FOUND);
        }
        Jugador jugador = jugadorOptional.get();
        List<JugadorClub> historial = jugadorClubService.historialJugador(id);
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
        Optional<Jugador> jugadorOptional = jugadorService.getJugadorPorId(id);
        if(!jugadorOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El jugador no existe"), HttpStatus.NOT_FOUND);
        }
        Jugador jugador = jugadorOptional.get();
        JugadorClub ultimoPase = jugadorService.getUltimaTransferencia(jugador);
        List<JugadorClub> historial = jugadorClubService.historialJugador(id);
        return new ResponseEntity<>(ultimoPase, HttpStatus.OK);
    }

    @GetMapping("ClubEnFecha/{id}")
    public ResponseEntity<?> clubEnCiertaFecha(@PathVariable ("id") Long id){
        Optional<Jugador> jugadorOptional = jugadorService.getJugadorPorId(id);
        if(!jugadorOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El jugador no existe"), HttpStatus.NOT_FOUND);
        }
        Jugador jugador = jugadorOptional.get();
        LocalDate fecha = LocalDate.of(2022, Month.MARCH, 30);
        Club club = jugadorService.getClubEnFecha(jugador, fecha);
        if(club == null){
            return new ResponseEntity<>(new Mensaje("No tiene clubes"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(club,HttpStatus.OK);
    }



}
