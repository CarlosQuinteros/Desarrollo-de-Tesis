package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.JugadorPartido;
import backendAdministradorCompetenciasFutbolisticas.Enums.PosicionJugador;
import backendAdministradorCompetenciasFutbolisticas.Enums.TipoRolJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JugadorPartidoRepository extends JpaRepository<JugadorPartido,Long> {


    //existe referencias con partido
    boolean existsByPartido_Id(Long idPartido);

    //existe cierta posicion (AR,DEF,MED,DEL) en partido por club y rol de jugador (Titular o suplente)
    boolean existsByPartido_IdAndClub_IdAndRolAndPosicion(Long idPartido, Long idClub, TipoRolJugador tipoRolJugador, PosicionJugador posicionJugador);

    //existe la participacion de un jugador en un partido
    boolean existsByPartido_IdAndJugador_Id(Long idPartido, Long idJugador);

    //determina si el jugador forma parte del equipo
    boolean existsByPartido_IdAndClub_IdAndJugador_Id(Long idPartido, Long idClub, Long idJugador);

    //determina si el jugador forma parte de titulares o suplentes en un club en partido
    boolean existsByPartido_IdAndClub_IdAndJugador_IdAndRol(Long idPartido, Long idClub, Long idJugador, TipoRolJugador tipoRolJugador);

    //cuenta la cantidad de jugadores por club y rol en un partido. ej. cantidad titulares del club local
    Integer countByPartido_IdAndClub_IdAndRol(Long idPartido, Long idClub, TipoRolJugador rolJugador);

    //obtiene las participaciones por club y rol en un partido. ej. jugadores titulares del club local
    List<JugadorPartido> findByPartido_IdAndClub_IdAndRol(Long idPartido, Long idClub, TipoRolJugador rolJugador);

    //obtiene todas las participaciones por club
    List<JugadorPartido> findByPartido_IdAndClub_Id(Long idPartido, Long idClub);

    //obtiene todas las participaciones de un jugador
    List<JugadorPartido> findByJugador_Id(Long idJugador);


}
