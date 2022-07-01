package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidoRepository extends JpaRepository<Partido,Long> {

    List<Partido> findByClubLocal_IdAndClubVisitante_IdOrClubLocal_IdAndClubVisitante_Id(Long idClubLocal, Long idClubVisitante, Long idClubVisitanteToLocal, Long idClubLocalToVisitante);

    boolean existsByIdAndClubLocal_IdOrClubVisitante_Id(Long idPartido, Long idClubLocal, Long idClubVisitante);
}
