package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findById(Long id);

    List<Club> findByOrderByLocalidad();

    List<Club> findByAsociacionDeportiva_Nombre(String nombreAsociacion);

    List<Club> findByAsociacionDeportiva_IdOrderByNombreClubAsc(Long idAsociacion);

    Optional<Club> findByEmail(String email);

    List<Club> findByLocalidad_IdOrderByNombreClubAsc(Long idLocalidad);

    List<Club> findByLocalidad_Nombre(String nombreLocalidad);

    List<Club> findByFechaFundacionBetween(Date fechaDesde, Date fechaHasta);

    boolean existsByEmail(String email);

    boolean existsByLocalidad_Id(Long id);

    boolean existsByAsociacionDeportiva_Id(Long id);

    boolean existsByNombreClubAndAndAsociacionDeportiva_Nombre(String nombreClub, String nombreAsociacion);
}
