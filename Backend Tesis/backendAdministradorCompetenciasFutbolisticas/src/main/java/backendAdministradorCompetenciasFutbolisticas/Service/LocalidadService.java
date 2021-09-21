package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Excepciones.LocalidadNoExisteException;
import backendAdministradorCompetenciasFutbolisticas.Dtos.LocalidadDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import backendAdministradorCompetenciasFutbolisticas.Entity.Provincia;
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

    public boolean save(Localidad localidad){
        return localidadRepository.save(localidad).getId()!=null;
    }

    public boolean actualizar(Localidad localidad, LocalidadDto localidadDto, Provincia provincia){
        localidad.setNombre(localidadDto.getNombre());
        localidad.setProvincia(provincia);
        return localidadRepository.save(localidad) != null;
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

    public Localidad getLocalidadPorID(Long id) throws Exception {
        Optional<Localidad> localidadOptional = localidadRepository.findById(id);
        if(!localidadOptional.isPresent()){
            throw new LocalidadNoExisteException("No existe la Localidad con el Id: " + id);
        }
        return localidadOptional.get();
    }
}
