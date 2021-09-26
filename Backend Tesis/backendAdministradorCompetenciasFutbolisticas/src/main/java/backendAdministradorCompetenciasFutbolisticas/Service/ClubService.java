package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClubService {

    @Autowired
    ClubRepository clubRepository;


    public boolean guardarNuevoClub(Club club) {
        return clubRepository.save(club).getId()!=null;
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

}
