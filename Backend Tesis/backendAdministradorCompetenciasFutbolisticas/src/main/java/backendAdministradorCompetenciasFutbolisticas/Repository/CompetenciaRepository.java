package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia,Long> {

    boolean existsByAsociacionDeportiva_Id(Long idAsociacionDeportiva);

    boolean existsByCategoria_Id(Long idCategoria);

    boolean existsByClubesParticipantesContains(Club club);

    Integer countCompetenciaBy();
}
