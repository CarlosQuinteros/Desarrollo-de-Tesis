package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorClub;
import backendAdministradorCompetenciasFutbolisticas.Repository.JugadorClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JugadorClubService {

    @Autowired
    JugadorClubRepository jugadorClubRepository;

    public boolean guardar(JugadorClub jugadorClub){
        return jugadorClubRepository.save(jugadorClub).getId() != null;
    }

    public boolean existeHistorialPorClub(Long id){
        return jugadorClubRepository.existsByClub_Id(id);
    }

    public List<JugadorClub> historialJugador(Long id){
        return jugadorClubRepository.findByJugador_IdOrderByFechaAsc(id);
    }

    public List<JugadorClub> historialExJugadoresPorIdClub(Long idClub){
        return jugadorClubRepository.findByClub_IdAndJugador_ClubActual_IdNot(idClub,idClub);
    }

}
