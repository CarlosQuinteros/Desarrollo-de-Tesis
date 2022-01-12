package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Jugador;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository  extends JpaRepository<Jugador, Long> {

    boolean existsByDocumento(String documento);

    Optional<Jugador> findByDocumento(String documento);

    Optional<Jugador> findById(Long id);

    List<Jugador> findByFechaNacimientoBetween(LocalDate fechaDesde, LocalDate fechaHasta);

    List<Jugador> findByOrderByApellidosAsc();

    List<Jugador> findByEstadoJugador_Id(Long idEstado);

    List<Jugador> findByEstadoJugador_EstadoJugador(NombreEstadoJugador nombreEstadoJugador);

    List<Jugador> findByClubActual_Id(Long id);

    Integer countJugadorBy();


}
