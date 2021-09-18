package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import backendAdministradorCompetenciasFutbolisticas.Entity.Provincia;
import backendAdministradorCompetenciasFutbolisticas.Repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocalidadService {
    @Autowired
    LocalidadRepository localidadRepository;

    public boolean save(Localidad localidad){
        return localidadRepository.save(localidad).getId()!=null;
    }

    public void eliminarLocalidad(Long id){ localidadRepository.deleteById(id);}

    public Optional<Localidad> getById(Long id) { return  localidadRepository.findById(id);}

    public List<Localidad> getListado(){

        return localidadRepository.findByOrderByProvinciaNombreAsc();
    }

    public List<Localidad> getLocalidadesPorIdProvincia(Long idProvincia){
        return localidadRepository.findByProvincia_IdOrderByNombreAsc(idProvincia);
    }

    public List<Localidad> getLocalidadesPorNombreProvincia(String nombreProvincia){
        return localidadRepository.findByProvincia_NombreOrderByNombre(nombreProvincia);
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
