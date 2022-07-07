package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Anotacion;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    List<Anotacion>findByPartido_IdAndClubAnota_Id(Long idPartido, Long idClubAnota);

    List<Anotacion> findByJugador_IdAndTipoGolNot(Long idJugador, NombreTipoGol nombreTipoGol);

    boolean existsByPartido_IdAndJugador_Id(Long idPartido, Long idJugador);

    List<Anotacion> findByPartidoJornadaCompetencia_Id(Long idCompetencia);
}
