package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.EstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoJugadorRepository extends JpaRepository<EstadoJugador, Long> {

    EstadoJugador findByEstadoJugador(NombreEstadoJugador nombreEstadoJugador);

}
