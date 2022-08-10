package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Interface.IGoleador;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Posicion;
import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoPartido;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.CompetenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class CompetenciaService {

    @Autowired
    private CompetenciaRepository competenciaRepository;

    @Autowired
    private ClubService clubService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AsociacionDeportivaService asociacionDeportivaService;

    @Autowired
    private JornadaService jornadaService;

    @Autowired
    private AnotacionService anotacionService;

    @Autowired
    private PartidoService partidoService;

    public Competencia guardarCompetencia(Competencia competencia){
        return competenciaRepository.save(competencia);
    }

    public Competencia getCompetencia(Long id){
        return competenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La competencia con ID: " + id + " no existe"));
    }

    public List<Competencia> getListadoCompetencias(){
        return competenciaRepository.findAll();
    }

    public void eliminarCompetencia(Long id){
        Competencia competencia = getCompetencia(id);
        if(jornadaService.existeReferenciasConCompetencia(competencia.getId())){
            throw new BadRequestException("La competencia tiene referencias con jornadas y no puede eliminarse");
        }
        competenciaRepository.deleteById(competencia.getId());
    }

    public boolean existeReferenciasConCategoria(Long idCategoria){
        return competenciaRepository.existsByCategoria_Id(idCategoria);
    }

    public boolean existeReferenciasConAsociacionDeportiva(Long idAsociacion){
        return competenciaRepository.existsByAsociacionDeportiva_Id(idAsociacion);
    }

    public Integer cantidadTotalCompetencias(){
        return competenciaRepository.countCompetenciaBy();
    }

    public List<IGoleador> goleadoresDeUnaCompetencia(Long idCompetencia){
        List<IGoleador> IGoleadores = anotacionService.goleadoresDeUnaCompetencia(idCompetencia)
                .stream()
                .limit(12)
                .collect(Collectors.toList());
        return IGoleadores;
    }

    public List<Posicion> tablaPosicionesDeUnaCompetencia(Long idCompetencia){
        Competencia competencia = getCompetencia(idCompetencia);
        List<DetalleGeneralPartidoDto> partidosPorCompetencia = partidoService.listadoDePartidosPorCompetencia(idCompetencia);
        Stream<String> clubesStream = Stream.concat(Stream.concat(partidosPorCompetencia.stream().map(DetalleGeneralPartidoDto::getClubLocal), partidosPorCompetencia.stream().map(DetalleGeneralPartidoDto::getClubVisitante)),competencia.getClubesParticipantes().stream().map(Club::getNombreClub));
        List<String> clubes = clubesStream.distinct().collect(Collectors.toList());
        List<Posicion> posiciones = new ArrayList<>();
        Comparator<Posicion> comparadorMultiple = Comparator.comparing(Posicion::getPTS)
                .thenComparing(Posicion::getDIF)
                .thenComparing(Posicion::getGF)
                .thenComparing(Posicion::getV)
                .thenComparing(Posicion::getClub)
                .reversed();

        clubes.forEach(club -> {
            Posicion posicion = new Posicion(club);
            partidosPorCompetencia.stream().filter(p -> p.getEstado().equals(NombreEstadoPartido.FINALIZADO.name())).forEach(partido -> {
                if(partido.getClubLocal().equals(club)){
                    if (partido.getCantidadGolesClubLocal() > partido.getCantidadGolesClubVisitante()) {
                        posicion.sumarPuntosVictoria();
                        posicion.sumarGolesAFavor(partido.getCantidadGolesClubLocal());
                        posicion.sumarGolesEnContra(partido.getCantidadGolesClubVisitante());
                        posicion.actualizarDiferencia();
                    }
                    if(partido.getCantidadGolesClubLocal() < partido.getCantidadGolesClubVisitante()){
                        posicion.sumarPuntosDerrota();
                        posicion.sumarGolesAFavor(partido.getCantidadGolesClubLocal());
                        posicion.sumarGolesEnContra(partido.getCantidadGolesClubVisitante());
                        posicion.actualizarDiferencia();
                    }
                    if (partido.getCantidadGolesClubLocal() == partido.getCantidadGolesClubVisitante()){
                        posicion.sumarPuntosEmpate();
                        posicion.sumarGolesAFavor(partido.getCantidadGolesClubLocal());
                        posicion.sumarGolesEnContra(partido.getCantidadGolesClubVisitante());
                        posicion.actualizarDiferencia();
                    }
                }
                if(partido.getClubVisitante().equals(club)){
                    if(partido.getCantidadGolesClubVisitante() > partido.getCantidadGolesClubLocal()){
                        posicion.sumarPuntosVictoria();
                        posicion.sumarGolesAFavor(partido.getCantidadGolesClubVisitante());
                        posicion.sumarGolesEnContra(partido.getCantidadGolesClubLocal());
                        posicion.actualizarDiferencia();
                    }
                    if(partido.getCantidadGolesClubVisitante() < partido.getCantidadGolesClubLocal()){
                        posicion.sumarPuntosDerrota();
                        posicion.sumarGolesAFavor(partido.getCantidadGolesClubVisitante());
                        posicion.sumarGolesEnContra(partido.getCantidadGolesClubLocal());
                        posicion.actualizarDiferencia();
                    }
                    if(partido.getCantidadGolesClubLocal() == partido.getCantidadGolesClubVisitante()){
                        posicion.sumarPuntosEmpate();
                        posicion.sumarGolesAFavor(partido.getCantidadGolesClubVisitante());
                        posicion.sumarGolesEnContra(partido.getCantidadGolesClubLocal());
                        posicion.actualizarDiferencia();
                    }
                }
            });
            posiciones.add(posicion);
        });
        List<Posicion> tabla = posiciones.stream().sorted(comparadorMultiple).collect(Collectors.toList());
        //tabla.forEach(p -> System.out.println(p.toString()));
        return tabla;
    }

    public Map<String, Long> tiposDegolesMasAnotadosDeUnaCompetencia(Long id){
        Competencia competencia = getCompetencia(id);
        List<Anotacion> anotaciones = anotacionService.anotacionesDeUnaCompetencia(competencia.getId());
        Map<String, Long> tipoDeGoles = anotaciones.stream()
                .collect(Collectors.groupingBy(anotacion -> anotacion.getTipoGol().name(), Collectors.counting()));
        return tipoDeGoles;
    }




}
