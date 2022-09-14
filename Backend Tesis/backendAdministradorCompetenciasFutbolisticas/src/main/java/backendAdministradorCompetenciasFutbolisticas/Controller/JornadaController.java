package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.JornadaDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Competencia;
import backendAdministradorCompetenciasFutbolisticas.Entity.Jornada;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Service.CompetenciaService;
import backendAdministradorCompetenciasFutbolisticas.Service.JornadaService;
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
@RequestMapping("/jornadas")
@CrossOrigin("*")
public class JornadaController {

    @Autowired
    private JornadaService jornadaService;

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private CompetenciaService competenciaService;

    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearJornada(@Valid @RequestBody JornadaDto jornadaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Competencia competencia = competenciaService.getCompetencia(jornadaDto.getIdCompetencia());
        Jornada nuevaJornada = new Jornada(jornadaDto.getNumero(), jornadaDto.getDescripcion(), competencia);
        jornadaService.guardarJornada(nuevaJornada);
        return new ResponseEntity<>(new Mensaje("Jornada guardada correctamente"),HttpStatus.CREATED);
    }

    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearJornada(@PathVariable Long id, @Valid @RequestBody JornadaDto jornadaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Competencia competencia = competenciaService.getCompetencia(jornadaDto.getIdCompetencia());
        Jornada jornada = jornadaService.getJornada(id);
        jornada.setDescripcion(jornadaDto.getDescripcion());
        jornada.setCompetencia(competencia);
        jornada.setNumero(jornadaDto.getNumero());

        jornadaService.guardarJornada(jornada);
        return new ResponseEntity<>(new Mensaje("Jornada guardada correctamente"),HttpStatus.OK);
    }

    @GetMapping("/{id}/partidos")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<DetalleGeneralPartidoDto>> listadoDePartidosPorJornada(@PathVariable ("id") Long id){
        Jornada jornada = jornadaService.getJornada(id);
        List<DetalleGeneralPartidoDto> partidosPorJornada = partidoService.listadoDePartidosPorJornada(jornada.getId());
        return new ResponseEntity<>(partidosPorJornada, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<Jornada> detalleJornada(@PathVariable Long id){
        Jornada jornada = jornadaService.getJornada(id);
        return new ResponseEntity<>(jornada,HttpStatus.OK);
    }


    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> eliminarJornada(@PathVariable Long id){
        jornadaService.eliminarJornada(id);
        return new ResponseEntity<>(new Mensaje("Jornada eliminada correctamente"),HttpStatus.OK);
    }
}
