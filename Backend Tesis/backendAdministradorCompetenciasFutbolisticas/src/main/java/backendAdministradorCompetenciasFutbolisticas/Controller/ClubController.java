package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.ClubDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Service.AsociacionDeportivaService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.PaseJugadorService;
import backendAdministradorCompetenciasFutbolisticas.Service.LocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    /*
        Metodo que permite crear un nuevo club
     */

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearClub(@Valid @RequestBody ClubDto clubDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("Campos mal ingresados"), HttpStatus.NOT_FOUND);
        }
        if(clubService.existePorNombre(clubDto.getNombre())){
            return new ResponseEntity<>(new Mensaje("Ya existe un Club con el nombre ingresado"), HttpStatus.NOT_FOUND);
        }
        if(clubService.existByEmail(clubDto.getEmail())){
            return new ResponseEntity<>(new Mensaje("Ya existe un Club con el email ingresado"), HttpStatus.NOT_FOUND);
        }
        try {
            Club nuevoClub = new Club(clubDto.getAlias(), clubDto.getNombre(), clubDto.getEmail());
            boolean resultado = clubService.guardarNuevoClub(nuevoClub);
            if(resultado){
                return new ResponseEntity<>(new Mensaje("Club guardado correctamente"),HttpStatus.CREATED);
            }
            return new ResponseEntity<>(new Mensaje("Club no guardado correctamten"), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. Club no guardado correctamten"), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(new Mensaje("El club no existe"), HttpStatus.NOT_FOUND);
        }
        if(paseJugadorService.existeHistorialPorClub(id)){
            return new ResponseEntity<>(new Mensaje("El club tiene transacciones con jugadores y no puede eliminarse"),HttpStatus.BAD_REQUEST);
        }
        try {
            clubService.eliminarClub(id);
            return new ResponseEntity<>(new Mensaje("Club eliminado correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. El club no se elimino"),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarClub(@PathVariable ("id") Long id, @Valid @RequestBody ClubDto clubDto , BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("campos mal ingresados"), HttpStatus.NOT_FOUND);
        }
        Optional<Club> clubOptional = clubService.getById(id);
        if(!clubOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El Club no existe"), HttpStatus.NOT_FOUND);
        }
        Club clubActualizar = clubOptional.get();
        if(!clubActualizar.getNombreClub().equals(clubDto.getNombre()) && clubService.existePorNombre(clubDto.getNombre())){
            return new ResponseEntity<>(new Mensaje("Ya existe un Club con el nombre ingresado"), HttpStatus.NOT_FOUND);
        }
        if(!clubActualizar.getEmail().equals(clubDto.getEmail()) && clubService.existByEmail(clubDto.getEmail())){
            return new ResponseEntity<>(new Mensaje("Ya existe un Club con el email ingresado"), HttpStatus.NOT_FOUND);
        }
        clubActualizar.setAlias(clubDto.getAlias());
        clubActualizar.setNombreClub(clubDto.getNombre());
        clubActualizar.setEmail(clubDto.getEmail());
        Club clubActualizado = clubService.actualizarClub(clubActualizar);
        if(clubActualizado != null){
            return new ResponseEntity<>(new Mensaje("Club actualizado correctamente", clubActualizado), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Mensaje("Club no actualizado correctamente"),HttpStatus.INTERNAL_SERVER_ERROR);
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
        Optional<Club> clubOptional = clubService.getById(id);
        if(!clubOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("El Club no existe"),HttpStatus.NOT_FOUND);
        }
        Club club = clubOptional.get();
        return new ResponseEntity<>(club, HttpStatus.OK);
    }

    @GetMapping("/exjugadores/{id}")
    public ResponseEntity<List<Jugador>> listadoExJugadoresPorClub(@PathVariable ("id") Long id){
        Optional<Club> clubOptional = clubService.getById(id);
        if(!clubOptional.isPresent()){
            return new ResponseEntity(new Mensaje("El Club no existe"),HttpStatus.NOT_FOUND);
        }
        List<Jugador> historialExJugadores = paseJugadorService.historialExJugadoresPorIdClub(id).stream().map(Pase::getJugador).collect(Collectors.toList());
        return new ResponseEntity<>(historialExJugadores,HttpStatus.OK);
    }

}
