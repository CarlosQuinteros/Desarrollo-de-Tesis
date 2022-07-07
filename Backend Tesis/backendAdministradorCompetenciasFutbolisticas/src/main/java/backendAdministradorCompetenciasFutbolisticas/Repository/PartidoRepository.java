package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidoRepository extends JpaRepository<Partido,Long> {

    List<Partido> findByClubLocal_IdAndClubVisitante_IdOrClubLocal_IdAndClubVisitante_Id(Long idClubLocal, Long idClubVisitante, Long idClubVisitanteToLocal, Long idClubLocalToVisitante);

    boolean existsByIdAndClubLocal_IdOrClubVisitante_Id(Long idPartido, Long idClubLocal, Long idClubVisitante);

    boolean existsByJornada_Id(Long idJornada);

    List<Partido> findByJornada_Id(Long idJornada);

    List<Partido> findByJornada_Competencia_Id(Long idCompetencia);

    boolean existsByJornada_Competencia_IdAndAndClubLocal_IdAndClubVisitante_Id(Long idCompetencia, Long idClubLocal, Long idClubVisitante);

    @Query(value = "select case when (count(p) > 0) then true else false end from Partido p where p.jornada_id = :idJornada and (p.club_local_id = :idClubLocal or p.club_visitante_id = :idClubVisitante)", nativeQuery = true)
    boolean existsByJornada_IdAndClubLocal_IdOrClubVisitante_Id(@Param("idJornada") Long idJornada, @Param("idClubLocal")Long idClubLocal,@Param("idClubVisitante") Long idClubVisitante);

    boolean existsByClubLocal_IdOrClubVisitante_Id(Long idClubLocal, Long idClubVisitante);


}
