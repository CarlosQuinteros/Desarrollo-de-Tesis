package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InternalServerErrorException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClubService {

    @Autowired
    ClubRepository clubRepository;


    public boolean guardarNuevoClub(Club club) {
        try {
            return clubRepository.save(club).getId()!=null;
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. Club no guardado correctamente");
        }
    }

    public Club actualizarClub(Club club){
        return clubRepository.save(club);
    }

    public boolean existById(Long id){
        return clubRepository.existsById(id);
    }

    public boolean existByEmail(String email){
        return  clubRepository.existsByEmail(email);
    }

    public boolean existePorNombre(String nombre){ return clubRepository.existsByNombreClub(nombre);}

    public Optional<Club> getById(Long id){
        return clubRepository.findById(id);
    }

    public Optional<Club> getByEmailClub(String email){
        return clubRepository.findByEmail(email);
    }

    public List<Club> getListado(){
        return clubRepository.findAll();
    }

    public void eliminarClub(Long id) { clubRepository.deleteById(id);}

    public List<Club> getListadoOrdenadoPorNombre(){
        return clubRepository.findByOrderByNombreClub();
    }

    public  Integer getCantidadDeClubes(){
        return  clubRepository.countClubBy();
    }

    public  Club getClub(Long id){
        Club club = getById(id).orElseThrow(()-> new ResourceNotFoundException("EL Club con ID: " + id + " no existe"));
        return club;
    }

}
