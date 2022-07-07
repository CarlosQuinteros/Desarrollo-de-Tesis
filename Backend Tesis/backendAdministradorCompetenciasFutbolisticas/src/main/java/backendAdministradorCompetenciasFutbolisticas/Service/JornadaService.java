package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Jornada;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.JornadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JornadaService {

    @Autowired
    private JornadaRepository jornadaRepository;

    @Autowired
    private PartidoService partidoService;

    public Jornada guardarJornada(Jornada nuevaJornada){
        return jornadaRepository.save(nuevaJornada);
    }

    public Jornada getJornada(Long id){
        return jornadaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La jornada con ID: " + id + " no existe"));
    }

    public List<Jornada> getListadoJornadasPorCompetencia(Long idCompetencia){
        return jornadaRepository.findByCompetencia_IdOrderByNumeroAsc(idCompetencia);
    }

    public void eliminarJornada(Long id){
        Jornada jornada = getJornada(id);
        if(partidoService.existeReferenciasConJornada(jornada.getId())){
            throw new BadRequestException("La jornada tiene referencias con partidos y no puede eliminarse");
        }
        jornadaRepository.deleteById(jornada.getId());
    }

    public boolean existeReferenciasConCompetencia(Long idCompetencia){
        return jornadaRepository.existsByCompetencia_Id(idCompetencia);
    }




}
