package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaseJugadorRepository extends JpaRepository<Pase, Long> {

    //  obtiene el pase vigente de un jugador, fechaHasta = null
    Pase findByJugador_IdAndFechaHastaIsNull(Long id);

    boolean existsByClub_Id(Long idClub);

    // obtiene el historial de pases de un jugador
    List<Pase> findByJugador_IdOrderByFechaAsc(Long id);

    //obtiene los exjugadores de un club
    List<Pase> findByClub_IdAndJugador_ClubActual_IdNot(Long idClub, Long id);

}
