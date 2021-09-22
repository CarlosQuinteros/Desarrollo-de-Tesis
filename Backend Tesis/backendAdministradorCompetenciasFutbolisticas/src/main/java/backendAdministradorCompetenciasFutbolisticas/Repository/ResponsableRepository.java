package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResponsableRepository extends JpaRepository<Responsable, Long> {


    boolean existsByDocumento(String documento);

    boolean existsByEmail(String email);

    Optional<Responsable> findByDocumento(String documento);

    List<Responsable> findByOrderByApellidoAscNombreAsc();

    List<Responsable> findByOrderByApellido();
}
