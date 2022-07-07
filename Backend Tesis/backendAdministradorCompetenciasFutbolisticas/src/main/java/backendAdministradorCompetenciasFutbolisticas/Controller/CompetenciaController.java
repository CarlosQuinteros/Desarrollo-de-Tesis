package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.CompetenciaDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoGenero;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/competencias")
@CrossOrigin("*")
public class CompetenciaController {

    @Autowired
    private CompetenciaService competenciaService;

    @Autowired
    private JornadaService jornadaService;

    @Autowired
    private AsociacionDeportivaService asociacionDeportivaService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AnotacionService anotacionService;

    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearCompetencia(@Valid @RequestBody CompetenciaDto competenciaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        AsociacionDeportiva asociacionDeportiva = asociacionDeportivaService.getById(competenciaDto.getIdAsociacionDeportiva());
        Categoria categoria = categoriaService.getDetalleCategoria(competenciaDto.getIdCategoria());
        TipoGenero tipoGenero = generoService.getTipoGeneroPorNombre(competenciaDto.getGenero());
        Competencia nuevaCompetencia =
                new Competencia(asociacionDeportiva,
                        categoria,
                        tipoGenero,
                        competenciaDto.getNombre(),
                        competenciaDto.getTemporada(),
                        competenciaDto.getDescripcion(),
                        competenciaDto.getTarjetasAmarillasPermitidas()
                );
        List<Club> clubesParticipantes = competenciaDto.getClubesParticipantes()
                .stream()
                .map(club -> clubService.getClub(club.getId()))
                .distinct()
                .collect(Collectors.toList());
        nuevaCompetencia.setClubesParticipantes(clubesParticipantes);
        competenciaService.guardarCompetencia(nuevaCompetencia);
        return new ResponseEntity<>(new Mensaje("Nueva competencia guardada correctamente"), HttpStatus.CREATED);
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> editarCompetencia(@PathVariable Long id, @Valid @RequestBody CompetenciaDto competenciaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Competencia competencia = competenciaService.getCompetencia(id);
        AsociacionDeportiva asociacionDeportiva = asociacionDeportivaService.getById(competenciaDto.getIdAsociacionDeportiva());
        Categoria categoria = categoriaService.getDetalleCategoria(competenciaDto.getIdCategoria());
        TipoGenero tipoGenero = generoService.getTipoGeneroPorNombre(competenciaDto.getGenero());

        List<Club> clubesParticipantes = competenciaDto.getClubesParticipantes()
                .stream()
                .map(club -> clubService.getClub(club.getId()))
                .distinct()
                .collect(Collectors.toList());

        competencia.setCategoria(categoria);
        competencia.setAsociacionDeportiva(asociacionDeportiva);
        competencia.setGenero(tipoGenero);
        competencia.setNombre(competenciaDto.getNombre());
        competencia.setTemporada(competenciaDto.getTemporada());
        competencia.setDescripcion(competenciaDto.getDescripcion());
        competencia.setTarjetasAmarillasPermitidas(competenciaDto.getTarjetasAmarillasPermitidas());
        competencia.setClubesParticipantes(clubesParticipantes);
        competenciaService.guardarCompetencia(competencia);
        return new ResponseEntity<>(new Mensaje("Competencia guardada correctamente"), HttpStatus.OK);
    }

    @GetMapping("/listado")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<Competencia>> listadoDeCompetencias(){
        List<Competencia> competencias = competenciaService.getListadoCompetencias();
        return new ResponseEntity<>(competencias,HttpStatus.OK);
    }

    @GetMapping("/{id}/jornadas")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<Jornada>> listadoJornadasDeCompetencia(@PathVariable Long id){
        Competencia competencia = competenciaService.getCompetencia(id);
        List<Jornada> jornadas = jornadaService.getListadoJornadasPorCompetencia(competencia.getId());
        return new ResponseEntity<>(jornadas, HttpStatus.OK);
    }

    @GetMapping("/{id}/goleadores")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> listadoDeGoleadoresDeCompetencia(@PathVariable Long id){
        Map<Jugador, List<Anotacion>> anotacionesPorJugador = anotacionService.anotacionesDeUnaCompetencia(id)
                .stream()
                .filter(anotacion -> !anotacion.getTipoGol().equals(NombreTipoGol.GOL_EN_CONTRA))
                .collect(Collectors.groupingBy(Anotacion::getJugador));
        return new ResponseEntity<>(anotacionesPorJugador, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> eliminarCompetencia(@PathVariable Long id){
        competenciaService.eliminarCompetencia(id);
        return new ResponseEntity<>(new Mensaje("Competencia eliminada correctamente"),HttpStatus.OK);
    }



}
