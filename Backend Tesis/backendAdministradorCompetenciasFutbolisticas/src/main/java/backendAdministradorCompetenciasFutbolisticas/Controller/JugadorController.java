package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.*;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InternalServerErrorException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.LogService;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

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

    @Autowired
    LogService logService;


    /*
    *   Metodo que permite crear un jugador, no debe existir el mismo documento
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearJugador(@Valid @RequestBody NuevoJugadorDto nuevoJugadorDto , BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        if(jugadorService.existeJugadorPorDocumento(nuevoJugadorDto.getDocumento())){
            throw new BadRequestException("Ya existe un jugador con el DNI: " + nuevoJugadorDto.getDocumento());
        }

        try {
            // El servicio genera not found exception si no obtiene el club
            Club club = clubService.getClub(nuevoJugadorDto.getIdClub());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.getUsuarioLogueado(auth);
            Jugador nuevoJugador = new Jugador(nuevoJugadorDto.getNombres(), nuevoJugadorDto.getApellidos(), nuevoJugadorDto.getDocumento(), nuevoJugadorDto.getFechaNacimiento() );
            nuevoJugador.setClubActual(club);
            Pase historiaClub = new Pase(nuevoJugadorDto.getFechaInscripcion(),null,nuevoJugador,club,"Inscripcion");
            //jugadorService.guardarNuevoJugador(nuevoJugador);
            boolean resultado = jugadorService.guardarNuevoJugador(nuevoJugador);
            if(resultado){
                paseJugadorService.guardar(historiaClub);
                logService.guardarLogCreacionJugador(nuevoJugador, historiaClub, usuario);
                return new ResponseEntity<>(new Mensaje("Jugador guardado correctamente"), HttpStatus.CREATED);
            }
            throw new InternalServerErrorException("Fallo la operación. Jugador no guardado correctamente");
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operación. Jugador no guardado correctamente");
        }

    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> actualizarJugador(@PathVariable ("id") Long id, @Valid @RequestBody EditarJugadorDto editarJugadorDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        // el servicio genera exception si no obtiene el jugador
        Jugador jugador = jugadorService.getJugador(id);

        if(!jugador.getDocumento().equals(editarJugadorDto.getDocumento()) && jugadorService.existeJugadorPorDocumento(editarJugadorDto.getDocumento())){
            throw new BadRequestException("Ya existe un jugador con el DNI: " + editarJugadorDto.getDocumento());
        }
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.getUsuarioLogueado(auth);
            jugador.setApellidos(editarJugadorDto.getApellidos());
            jugador.setNombre((editarJugadorDto.getNombres()));
            jugador.setDocumento(editarJugadorDto.getDocumento());
            jugador.setFechaNacimiento(editarJugadorDto.getFechaNacimiento());
            Jugador jugadorActualizado = jugadorService.guardarJugador(jugador);
            if(jugadorActualizado != null){
                logService.guardarLogModificacionJugador(jugador, usuario);
                return new ResponseEntity<>(new Mensaje("Jugador guardado correctamente", jugador), HttpStatus.CREATED);
            }
            throw new InternalServerErrorException("Fallo la operación. Jugador no guardado correctamente");
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operación. Jugador no guardado correctamente");
        }

    }

    /*
    *   Metodo que retorna el listado de jugadores ordenado por apellido
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES', 'USER')")
    @GetMapping("/listado")
    public ResponseEntity<List<Jugador>> listadoJugadores(){
        List<Jugador> listado = jugadorService.getListadoJugadoresOrdenadoPorApellido();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }


    /*
    *   Metodo que retorna el detalle de un jugador
    */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<DetalleJugadorDto> detalleJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);

        List<Pase> historialClubes = paseJugadorService.historialJugador(id);

        DetalleJugadorDto detalleJugador = new DetalleJugadorDto();
        detalleJugador.setJugador(jugador);
        detalleJugador.setHistorialClubes(historialClubes);
        return new ResponseEntity<>(detalleJugador, HttpStatus.OK);
    }

    @GetMapping("/detalle/dni/{documento}")
    public ResponseEntity<DetalleJugadorDto> detalleJugadorPorDni(@PathVariable("documento") String documento){
        //el servicio genera exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugadorPorDni(documento);
        List<Pase> historialClubes = paseJugadorService.historialJugador(jugador.getId());

        DetalleJugadorDto detalleJugador = new DetalleJugadorDto();
        detalleJugador.setJugador(jugador);
        detalleJugador.setHistorialClubes(historialClubes);
        return new ResponseEntity<>(detalleJugador, HttpStatus.OK);
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

        //el servicio genera badRequest exception si el jugador ya esta retirado
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
    @PostMapping("/pases/nuevo")
    public ResponseEntity<?> cambioDeClubJugador(@Valid @RequestBody CambioDeClubDto cambioDeClubDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }

        //el servicio genera notFound exception si no encuentra el jugador o el club
        Jugador jugador = jugadorService.getJugador(cambioDeClubDto.getIdJugador());
        Club club = clubService.getClub(cambioDeClubDto.getIdClub());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);

        LocalDate fechaDesde = cambioDeClubDto.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        //valida fecha ingresada de cambio de club, validar fecha genera badrequestException si no es valida
        jugadorService.validarFechaCambioDeClub(fechaDesde, jugador);

       //valida que el club ingresado no sea el el club actual del jugador, genera badrequest si es el caso
       jugadorService.validarClubNoIgualesAlCambiarDeClub(club,jugador);
        try {
            Pase ultimoPase = jugadorService.getUltimaTransferencia(jugador.getId());
            if(ultimoPase!= null && ultimoPase.getFechaHasta() == null){
                ultimoPase.setFechaHasta(fechaDesde);
                paseJugadorService.guardar(ultimoPase);
            }

            Pase nuevaHistoriaClub = new Pase(fechaDesde, null, jugador, club, cambioDeClubDto.getMotivo());
            if(paseJugadorService.guardar(nuevaHistoriaClub)) {
                jugador.setClubActual(club);
                jugadorService.save(jugador);
                logService.guardarLogCreacionPase(nuevaHistoriaClub, usuario);
            }
            return new ResponseEntity<>(new Mensaje("Se cambio el club del jugador correctamente", nuevaHistoriaClub), HttpStatus.OK);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. No se registró el cambio de club");
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/pases/editar/{idPase}")
    public ResponseEntity<?> editarPase(@PathVariable ("idPase") Long idPase, @Valid @RequestBody EditarPaseJugadorDto editarPaseJugadorDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);

        //El servicio genera exception si no encuentra el pase
        Pase paseJugador = paseJugadorService.getPaseById(idPase);

        //El servicio genera exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(paseJugador.getJugador().getId());

        Pase ultimoPase = jugadorService.getUltimaTransferencia(jugador.getId());

        Club clubActualizado = clubService.getClub(editarPaseJugadorDto.getIdClub());

        if(ultimoPase != null && ultimoPase.getFechaHasta() == null && !ultimoPase.getId().equals(paseJugador.getId()) && editarPaseJugadorDto.getFechaHasta() == null){
            throw new BadRequestException("No se puede establecer una fecha vacia en un pase que no sea el actual del jugador.");
        }

        if(jugador.getClubActual() == null && editarPaseJugadorDto.getFechaHasta() == null){
            throw new BadRequestException("El jugador actualmente esta sin club. Debes generar un nueva pase para establecer su club actual");
        }
        if(paseJugador.getFechaHasta() == null && paseJugador.getClub().getId() != clubActualizado.getId()){
            //System.out.println("La fecha hasta es null y clubes diferentes, debo actualizar club actual al club: " + clubActualizado.getNombreClub());
            jugador.setClubActual(clubActualizado);
        }
        if(paseJugador.getFechaHasta() == null && editarPaseJugadorDto.getFechaHasta() != null){
            //System.out.println("la fecha hasta del pase es null y viene del dto una fecha, debo actualizar club del jugador a null");
            jugador.setClubActual(null);
        }
        //System.out.println("no hay restricciones y puedo guardar el pase actualizado");
        paseJugador.setFecha(LocalDate.now());
        paseJugador.setClub(clubActualizado);
        paseJugador.setFechaDesde(editarPaseJugadorDto.getFechaDesde());
        paseJugador.setFechaHasta(editarPaseJugadorDto.getFechaHasta());
        paseJugador.setMotivo(editarPaseJugadorDto.getMotivo());
        paseJugadorService.guardar(paseJugador);
        jugadorService.guardarJugador(jugador);
        logService.guardarLogEdicionPase(paseJugador, usuario);
        return new ResponseEntity<>(new Mensaje("Pase guardado correctamente"),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @DeleteMapping("/pases/eliminar/{idPase}")
    public ResponseEntity<?> eliminarPase(@PathVariable("idPase") Long idPase){
        //el servicio genera exception si no obtiene el pase
        Pase pase = paseJugadorService.getPaseById(idPase);
        try{
            paseJugadorService.eliminarPase(pase.getId());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.getUsuarioLogueado(auth);
            logService.guardarLogEliminacionPase(pase, usuario);
            return new ResponseEntity<>(new Mensaje("Pase eliminado correctamente"), HttpStatus.OK);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. No se elimino el Pase");
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
    @GetMapping("/club/{idClub}")
    public ResponseEntity<List<Jugador>> listaJugadoresDeUnClub(@PathVariable ("idClub") Long idClub){
        Club club = clubService.getClub(idClub);
        List<Jugador> jugadores = paseJugadorService.jugadoresActualesPorClub(club.getId());
        return new ResponseEntity<>(jugadores,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/exjugadores/club/{id}")
    public ResponseEntity<List<Jugador>> listadoExJugadoresPorClub(@PathVariable ("id") Long id){
        //el servicio genera notfound exception si no encuentra el club
        Club club = clubService.getClub(id);
        List<Jugador> historialExJugadores = paseJugadorService.exJugadoresPorClub(club.getId());
        return new ResponseEntity<>(historialExJugadores,HttpStatus.OK);
    }

    @GetMapping("inscripcion/{id}")
    public ResponseEntity<?> inscripcionJugador(@PathVariable ("id") Long id){
        // el servicio genera notfound exception si no encuentra el jugador
        Jugador jugador = jugadorService.getJugador(id);

        Pase primerPase = jugadorService.getPrimerTransferencia(id);
        return new ResponseEntity<>(primerPase, HttpStatus.OK);
    }

    @GetMapping("ultimoPaseJugador/{id}")
    public ResponseEntity<?> ultimaTransferenciaJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador =jugadorService.getJugador(id);

        Pase ultimoPase = jugadorService.getUltimaTransferencia(jugador.getId());
        return new ResponseEntity<>(ultimoPase, HttpStatus.OK);
    }

    @GetMapping("clubActual/{id}")
    public ResponseEntity<Club> clubActualDeJugador(@PathVariable ("id") Long id){

        //el servicio genera notFound exception si no encuentra el jugador
        Jugador jugador =jugadorService.getJugador(id);

        Club club = jugadorService.getClubActualJugador(jugador.getId());
        return new ResponseEntity<>(club, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_JUGADORES', 'USER')")
    @GetMapping("/cantidadTotal")
    public  ResponseEntity<Integer> cantidadJugadores(){
        Integer cantidad = jugadorService.cantidadJugadores();
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }




}
