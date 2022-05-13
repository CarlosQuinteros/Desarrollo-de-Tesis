package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsociacionDeportivaRepository extends JpaRepository<AsociacionDeportiva, Long> {

    Optional<AsociacionDeportiva> findByNombre(String nombre);

    List<AsociacionDeportiva> findByOrderByNombre();

    boolean existsByNombre(String nombre);

    boolean existsByEmail(String email);
}
