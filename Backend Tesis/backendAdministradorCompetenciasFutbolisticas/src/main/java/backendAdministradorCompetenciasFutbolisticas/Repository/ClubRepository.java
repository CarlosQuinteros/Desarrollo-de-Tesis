package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findById(Long id);

    Optional<Club> findByEmail(String email);

    boolean existsByNombreClub(String nombre);

    boolean existsByEmail(String email);

    List<Club> findByOrderByNombreClub();

    Integer countClubBy();



}
