package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Repository.PaseJugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaseJugadorService {

    @Autowired
    PaseJugadorRepository paseJugadorRepository;

    public boolean guardar(Pase pase){
        return paseJugadorRepository.save(pase).getId() != null;
    }

    public boolean existeHistorialPorClub(Long id){
        return paseJugadorRepository.existsByClub_Id(id);
    }

    public List<Pase> historialJugador(Long id){
        return paseJugadorRepository.findByJugador_IdOrderByFechaAsc(id);
    }

    public List<Pase> historialExJugadoresPorIdClub(Long idClub){
        return paseJugadorRepository.findByClub_IdAndJugador_ClubActual_IdNot(idClub,idClub);
    }

    public void eliminarHistorialDeUnJugador(Jugador jugador){
        List<Pase> historial = historialJugador(jugador.getId());
        paseJugadorRepository.deleteAll(historial);
    }



}
