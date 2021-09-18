package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
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
        return clubRepository.save(club).getId()!=null;
    }

    public void actualizarClub(Club club){
        clubRepository.save(club);
    }

    public boolean existById(Long id){
        return clubRepository.existsById(id);
    }

    public boolean existByEmail(String email){
        return  clubRepository.existsByEmail(email);
    }

    Optional<Club> getById(Long id){
        return clubRepository.findById(id);
    }

    Optional<Club> getByEmailClub(String email){
        return clubRepository.findByEmail(email);
    }

    List<Club> getListado(){
        return clubRepository.findAll();
    }
    List<Club> getListadoPorIdAsociacion(Long idAsociacion){
        return  clubRepository.findByAsociacionDeportiva_IdOrderByNombreClubAsc(idAsociacion);
    }
    List<Club> getListadoOrdenadoPorLocalidad(){
        return clubRepository.findByOrderByLocalidad();
    }

    List<Club> getListadoPorIdLocalidad(Long idLocalidad){
        return  clubRepository.findByLocalidad_IdOrderByNombreClubAsc(idLocalidad);
    }






}
