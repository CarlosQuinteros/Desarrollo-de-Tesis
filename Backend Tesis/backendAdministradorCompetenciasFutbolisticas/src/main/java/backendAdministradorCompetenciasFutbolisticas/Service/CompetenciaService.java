package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Interface.IGoleador;
import backendAdministradorCompetenciasFutbolisticas.Entity.*;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.CompetenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


}
