package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Juez;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JuezRepository extends JpaRepository<Juez, Long> {
    Optional<Juez> findByDocumento(String documento);

    Optional<Juez> findByLegajo(String legajo);

    Optional<Juez> findByDocumentoOrAndLegajo(String documentoOrLegajo, String documentoOrlegajo);

    List<Juez> findByOrderByApellidos();

    Boolean existsByDocumento(String documento);

    Boolean existsByLegajo(String legajo);

    Integer countJuezBy();
}
