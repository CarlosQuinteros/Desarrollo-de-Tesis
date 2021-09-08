package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import backendAdministradorCompetenciasFutbolisticas.Repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocalidadService {
    @Autowired
    LocalidadRepository localidadRepository;

    public void save(Localidad localidad){
        localidadRepository.save(localidad);
    }

    public void eliminarLocalidad(Long id){ localidadRepository.deleteById(id);}

    public Optional<Localidad> getById(Long id) { return  localidadRepository.findById(id);}

    public List<Localidad> getListado(){
        return localidadRepository.findByOrderByProvinciaNombreAsc();
    }

    public boolean existById(Long id){
        return localidadRepository.existsById(id);
    }

    public boolean existeLocalidadNombreYProvinciaNombre(String nombreLocalidad, String nombreProvincia){
        return localidadRepository.existsByNombreAndAndProvincia_Nombre(nombreLocalidad, nombreProvincia);
    }

    public Optional<Localidad> getByNombre(String nombre){
        return localidadRepository.findByNombre(nombre);
    }
}
