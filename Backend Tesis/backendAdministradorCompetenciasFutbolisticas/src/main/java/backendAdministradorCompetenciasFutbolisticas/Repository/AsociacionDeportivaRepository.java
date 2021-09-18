package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsociacionDeportivaRepository extends JpaRepository<AsociacionDeportiva, Long> {

    Optional<AsociacionDeportiva> findByNombre(String nombre);

    List<AsociacionDeportiva> findByOrderByNombre();

    boolean existsByNombre(String nombre);
}
