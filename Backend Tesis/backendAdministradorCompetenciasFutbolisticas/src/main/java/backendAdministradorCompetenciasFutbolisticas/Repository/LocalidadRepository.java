package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    List<Localidad> findByOrderByProvinciaNombreAsc();

    Optional<Localidad> findByNombre(String nombre);

    boolean existsByNombreAndAndProvincia_Nombre(String nombreLocalidad, String nombreProvincia);
}
