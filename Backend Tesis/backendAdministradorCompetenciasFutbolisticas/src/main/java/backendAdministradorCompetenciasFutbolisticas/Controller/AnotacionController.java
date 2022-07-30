package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.AnotacionDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Anotacion;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoAnotacion;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Service.AnotacionService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.JugadorService;
import backendAdministradorCompetenciasFutbolisticas.Service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/anotaciones")
@CrossOrigin("*")

public class AnotacionController {

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private AnotacionService anotacionService;

    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearNuevaAnotacion(@Valid @RequestBody AnotacionDto nuevaAnotacion, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Partido partido = partidoService.getDetallePartido(nuevaAnotacion.getIdPartido());
        Club clubAnota = clubService.getClub(nuevaAnotacion.getIdClubAnota());
        Jugador jugadorAnota = jugadorService.getJugador(nuevaAnotacion.getIdJugadorAnota());
        NombreTipoGol tipoGol = anotacionService.getTipoAnotacionPorNombre(nuevaAnotacion.getTipoGol());

        Anotacion anotacion = new Anotacion(partido,jugadorAnota,clubAnota,nuevaAnotacion.getMinuto(),tipoGol);
        anotacionService.guardarAnotacion(anotacion);

        return new ResponseEntity<>(new Mensaje("Anotacion guardada correctamente"), HttpStatus.CREATED);
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> editarAnotacion(@PathVariable ("id") Long id, @Valid @RequestBody AnotacionDto editarAnotacion, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Anotacion anotacion = anotacionService.getAnotacion(id);
        Partido partido = partidoService.getDetallePartido(editarAnotacion.getIdPartido());
        Club clubAnota = clubService.getClub(editarAnotacion.getIdClubAnota());
        Jugador jugadorAnota = jugadorService.getJugador(editarAnotacion.getIdJugadorAnota());
        NombreTipoGol tipoGol = anotacionService.getTipoAnotacionPorNombre(editarAnotacion.getTipoGol());

        anotacion.setClubAnota(clubAnota);
        anotacion.setJugador(jugadorAnota);
        anotacion.setMinuto(editarAnotacion.getMinuto());
        anotacion.setTipoGol(tipoGol);
        anotacionService.guardarAnotacion(anotacion);

        return new ResponseEntity<>(new Mensaje("Anotacion guardada correctamente"), HttpStatus.OK);
    }

    @GetMapping("/tipo-goles")
    public ResponseEntity<List<String>> listadoTipoDeGoles(){
        List<String> tipoGoles = anotacionService.getListadoStringTipoAnotaciones();
        return new ResponseEntity<>(tipoGoles,HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarAnotacion(@PathVariable Long id){
        anotacionService.eliminarAnotacion(id);
        return new ResponseEntity<>(new Mensaje("Anotacion eliminada correctamente"),HttpStatus.OK);
    }





}
