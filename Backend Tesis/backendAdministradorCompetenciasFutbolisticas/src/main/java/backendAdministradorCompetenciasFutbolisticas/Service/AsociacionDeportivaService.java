package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.AsociacionDeportivaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AsociacionDeportivaService {

    @Autowired
    AsociacionDeportivaRepository asociacionDeportivaRepository;

    public boolean guardarNuevaAsociacion(AsociacionDeportiva asociacionDeportiva){
        return asociacionDeportivaRepository.save(asociacionDeportiva).getNombre() != null;
    }

    public AsociacionDeportiva actualizarAsociacion(AsociacionDeportiva asociacionDeportiva){
        return asociacionDeportivaRepository.save(asociacionDeportiva);
    }

    public void eliminarAsociacion(AsociacionDeportiva asociacionDeportiva){
        asociacionDeportivaRepository.delete(asociacionDeportiva);
    }

    public AsociacionDeportiva getById(Long id){
        return asociacionDeportivaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No existe una Asociacion con ID: " + id));
    }

    public List<AsociacionDeportiva> getListadoOrdenadoPorNombre(){
        return  asociacionDeportivaRepository.findByOrderByNombre();
    }

    public boolean existePorNombre(String nombre){
        return asociacionDeportivaRepository.existsByNombre(nombre);
    }

    public boolean existePorEmail(String email){
        return  asociacionDeportivaRepository.existsByEmail(email);
    }

}
