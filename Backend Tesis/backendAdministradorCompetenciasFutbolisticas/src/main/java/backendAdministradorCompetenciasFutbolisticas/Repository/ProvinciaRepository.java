package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    boolean existsByNombre(String nombre);
    List<Provincia> findByOrderByNombreAsc();
    Optional<Provincia> findByNombre(String nombre);
}
