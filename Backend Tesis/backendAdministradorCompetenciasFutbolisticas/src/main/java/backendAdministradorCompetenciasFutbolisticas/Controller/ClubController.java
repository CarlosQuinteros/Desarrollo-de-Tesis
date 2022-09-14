package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.ClubDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Log;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Enums.LogAccion;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InternalServerErrorException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clubes")
@CrossOrigin("*")
public class ClubController {


    @Autowired
    ClubService clubService;

    @Autowired
    LocalidadService localidadService;

    @Autowired
    AsociacionDeportivaService asociacionDeportivaService;

    @Autowired
    PaseJugadorService paseJugadorService;

    @Autowired
    LogService logService;

    @Autowired
    UsuarioService usuarioService;


    /*
        Metodo que permite crear un nuevo club
     */

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearClub(@Valid @RequestBody ClubDto clubDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        if(clubService.existePorNombre(clubDto.getNombre())){
            throw new BadRequestException("Ya existe un Club con el nombre ingresado");
        }
        if(clubService.existByEmail(clubDto.getEmail())){
            throw new BadRequestException("Ya existe un Club con el email ingresado");
        }
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.getUsuarioLogueado(auth);
            Club nuevoClub = new Club(clubDto.getAlias(), clubDto.getNombre(), clubDto.getEmail());
            boolean resultado = clubService.guardarNuevoClub(nuevoClub);
            if(resultado){
                logService.guardarLogCreacionClub(nuevoClub, usuario);
                return new ResponseEntity<>(new Mensaje("Club guardado correctamente"),HttpStatus.CREATED);
            }
            throw new InternalServerErrorException("Fallo la operacion. El club no se guardo correctamente");
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. El club no se guardo correctamente");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES', 'ENCARGADO_DE_TORNEOS')")
    @GetMapping("/listado")
    public ResponseEntity<List<Club>> listadoClubes(){
        List<Club>  listado = clubService.getListadoOrdenadoPorNombre();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarClub(@PathVariable ("id") Long id){
        if(!clubService.existById(id)){
            throw new BadRequestException("El club con ID: " + id +" no existe");
        }
        if(paseJugadorService.existeHistorialPorClub(id)){
            throw new BadRequestException("El club tiene transacciones con jugadores y no puede eliminarse");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        clubService.eliminarClub(id);
        logService.guardarLogEliminacionClub(id,usuario);
        return new ResponseEntity<>(new Mensaje("Club eliminado correctamente"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarClub(@PathVariable ("id") Long id, @Valid @RequestBody ClubDto clubDto , BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        //el servicio genera notfound exception si no encuentra el club
        Club clubActualizar = clubService.getClub(id);
        if(!clubActualizar.getNombreClub().equals(clubDto.getNombre()) && clubService.existePorNombre(clubDto.getNombre())){
            throw new BadRequestException("Ya existe un Club con el nombre ingresado");
        }
        if(!clubActualizar.getEmail().equals(clubDto.getEmail()) && clubService.existByEmail(clubDto.getEmail())){
            throw new BadRequestException("Ya existe un Club con el email ingresado");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        clubActualizar.setAlias(clubDto.getAlias());
        clubActualizar.setNombreClub(clubDto.getNombre());
        clubActualizar.setEmail(clubDto.getEmail());
        Club clubActualizado = clubService.actualizarClub(clubActualizar);
        if(clubActualizado != null){
            logService.guardarLogActualizacionClub(clubActualizado, usuario);
            return new ResponseEntity<>(new Mensaje("Club guardado correctamente", clubActualizado), HttpStatus.OK);
        }
            throw new InternalServerErrorException("Club no actualizado correctamente");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/cantidadTotal")
    public ResponseEntity<Integer> cantidadTotalDeClubes(){
        Integer cantidad = clubService.getCantidadDeClubes();
        return new ResponseEntity<>(cantidad, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> detalleClub(@PathVariable ("id") Long id){

        //el servicio genera notfound exception si no encuentra el club
        Club club = clubService.getClub(id);
        return new ResponseEntity<>(club, HttpStatus.OK);
    }



}
