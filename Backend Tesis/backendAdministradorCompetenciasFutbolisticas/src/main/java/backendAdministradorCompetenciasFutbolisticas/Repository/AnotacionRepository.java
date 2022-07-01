package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Anotacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    List<Anotacion>findByPartido_IdAndClubAnota_Id(Long idPartido, Long idClubAnota);

    List<Anotacion> findByJugador_Id(Long idJugador);
}
