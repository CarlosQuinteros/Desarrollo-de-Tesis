package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import backendAdministradorCompetenciasFutbolisticas.Entity.Provincia;
import backendAdministradorCompetenciasFutbolisticas.Repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProvinciaService {

    @Autowired
    ProvinciaRepository provinciaRepository;

    public void save(Provincia provincia){
        provinciaRepository.save(provincia);
    }

    public List<Provincia> getProvincias(){
        List<Provincia> listado =provinciaRepository.findByOrderByNombreAsc();
        return listado;
    }

    public Optional<Provincia> getById(Long id){
        return  provinciaRepository.findById(id);
    }

    public Optional<Provincia> getByNombre(String nombre){
        return provinciaRepository.findByNombre(nombre);
    }

    public boolean existById(Long id){
        return provinciaRepository.existsById(id);
    }

    public boolean existByNombre(String nombre){
        return provinciaRepository.existsByNombre(nombre);
    }

    public void eliminarProvincia(Long id) { provinciaRepository.deleteById(id);}

}
