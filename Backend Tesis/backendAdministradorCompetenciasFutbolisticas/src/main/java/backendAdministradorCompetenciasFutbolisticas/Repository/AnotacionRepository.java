package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Anotacion;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Interface.IGoleador;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreTipoGol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    List<Anotacion>findByPartido_IdAndClubAnota_Id(Long idPartido, Long idClubAnota);

    List<Anotacion> findByJugador_IdAndTipoGolNot(Long idJugador, NombreTipoGol nombreTipoGol);

    boolean existsByPartido_IdAndJugador_Id(Long idPartido, Long idJugador);

    @Query(value = "select * from anotacion a " +
            "join partido p on (a.partido_id = p.id) " +
            "join jornada j on (p.jornada_id = j.id) " +
            "join competencia c on(j.competencia_id = c.id)" +
            "where c.id = :idCompetencia" +
            "", nativeQuery = true)
    List<Anotacion> findByPartidoJornadaCompetencia_Id(@Param("idCompetencia") Long idCompetencia);

    @Query(value = "select c2.nombre_club as nombreClub,j2.apellidos as apellidos,j2.nombre as nombre,count(j2.id) as goles "+
            "from anotacion as a " +
            "join club as c2 on (a.club_anota_id = c2.id) " +
            "join jugador as j2 on (a.jugador_id = j2.id) " +
            "join partido as p on (p.id = a.partido_id) " +
            "join jornada as j on (j.id = p.jornada_id) " +
            "join competencia as c on (c.id = j.competencia_id) " +
            "where c.id = :idCompetencia and a.tipo_gol != 'GOL_EN_CONTRA' " +
            "group by c2.nombre_club,j2.id ,j2.apellidos,j2.nombre " +
            "order by count(j2.id) desc", nativeQuery = true)
    List<IGoleador> findGoleadoresByCompetencia_Id(@Param("idCompetencia") Long idCompetencia);


}
