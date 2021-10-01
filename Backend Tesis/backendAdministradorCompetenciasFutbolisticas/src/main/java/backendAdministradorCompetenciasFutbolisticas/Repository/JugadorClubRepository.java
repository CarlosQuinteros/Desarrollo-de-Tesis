package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JugadorClubRepository extends JpaRepository<JugadorClub, Long> {


    boolean existsByClub_Id(Long idClub);

    List<JugadorClub> findByJugador_IdOrderByFechaAsc(Long id);

    List<JugadorClub> findByClub_IdAndJugador_ClubActual_IdNot(Long idClub, Long id);

}
