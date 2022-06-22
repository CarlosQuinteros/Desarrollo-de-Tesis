package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Juez;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreRolJuez;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.JuezRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JuezService {

    @Autowired
    private JuezRepository juezRepository;

    @Autowired
    private JuezRolService juezRolService;

    public List<Juez> getListadoJueces() {
        return juezRepository.findByOrderByApellidos();
    }

    public boolean guardarNuevoJuez(Juez juez){
        return juezRepository.save(juez).getId() != null;
    }

    public Juez guardarJuez(Juez juez){
        return  juezRepository.save(juez);
    }

    public void eliminarJuez(Long id){
        if(juezRolService.existeParticipacionDeJuez(id)){
            throw new BadRequestException("El juez tiene referencias con partidos y no puede eliminarse");
        }
        juezRepository.deleteById(id);
    }

    public Juez getJuezPorId(Long id){
        return juezRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un juez con el ID: " + id));
    }

    public Juez getJuezPorDocumento(String documento){
        return juezRepository.findByDocumento(documento)
                .orElseThrow(()-> new ResourceNotFoundException("No existe un Juez con el documento" + documento));
    }

    public  Juez getJuezPorLegajo(String legajo){
        return juezRepository.findByLegajo(legajo)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un Juez con el legajo " + legajo));
    }

    public Juez getJuezPorDocumentoOrLegajo(String documentoOrLegajo){
        return juezRepository.findByDocumentoOrAndLegajo(documentoOrLegajo, documentoOrLegajo)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un Juez con el documento o legajo: " + documentoOrLegajo));
    }

    public List<String> getListadoNombreRolJuez(){
        List<String> roles = Arrays.stream(NombreRolJuez.values())
                .map(NombreRolJuez::name)
                .collect(Collectors.toList());
        return roles;
    }

    public Boolean existeJuezPorId(Long id){
        return juezRepository.existsById(id);
    }

    public Boolean existeJuezPorDocumento(String documento){
        return juezRepository.existsByDocumento(documento);
    }

    public Boolean existeJuezPorLegajo(String legajo){
        return juezRepository.existsByLegajo(legajo);
    }

    public Integer getCantidadTotalJueces(){
        return juezRepository.countJuezBy();
    }

}
