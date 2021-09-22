package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Responsable;
import backendAdministradorCompetenciasFutbolisticas.Repository.ResponsableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResponsableService {

    @Autowired
    ResponsableRepository responsableRepository;

    public Optional<Responsable> getById(Long id){
        return responsableRepository.findById(id);
    }

    public Optional<Responsable> getByDocumento(String documento){
        return responsableRepository.findByDocumento(documento);
    }

    public List<Responsable> getListadoOrdenadoPorApellido() {
        return responsableRepository.findByOrderByApellidoAscNombreAsc();
    }

    public boolean existeResponsablePorDocumento(String documento){
        return responsableRepository.existsByDocumento(documento);
    }

    public  boolean existeResponsablePorEmail(String email){
        return responsableRepository.existsByEmail(email);
    }
}
