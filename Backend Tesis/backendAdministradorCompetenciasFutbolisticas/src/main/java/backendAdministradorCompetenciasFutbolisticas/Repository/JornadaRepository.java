package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Jornada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JornadaRepository extends JpaRepository<Jornada,Long> {

    List<Jornada> findByCompetencia_IdOrderByNumeroAsc(Long idCompetencia);

    boolean existsByCompetencia_Id(Long idCompetencia);
}
