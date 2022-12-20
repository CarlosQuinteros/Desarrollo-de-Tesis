package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Sustitucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SustitucionRepository extends JpaRepository<Sustitucion,Long> {

    //determina si existe un cambio por el jugador que sale y club.
    boolean existsByPartido_IdAndClubSustituye_IdAndJugadorSale_Id(Long idPartido, Long idClub, Long idJugadorSale);

    //determina si el jugador salio
    boolean existsByPartido_IdAndJugadorSale_Id(Long idPartido, Long idJugador);

    Optional<Sustitucion> findByPartido_IdAndJugadorSale_Id(Long idPartido, Long idJugador);

    Optional<Sustitucion> findByPartido_IdAndJugadorEntra_Id(Long idPartido, Long idJugador);

    //determina si el jugador entró
    boolean existsByPartido_IdAndJugadorEntra_Id(Long idPartido, Long idJugador);

    //determina si existe un cambio por el jugador que entra y club
    boolean existsByPartido_IdAndClubSustituye_IdAndJugadorEntra_Id(Long idPartido, Long idClub, Long idJugadorEntra);

    //determina las susticiones realizadas por un club en un partido
    List<Sustitucion> findByPartido_IdAndClubSustituye_Id(Long idPartido, Long idClub);

}
