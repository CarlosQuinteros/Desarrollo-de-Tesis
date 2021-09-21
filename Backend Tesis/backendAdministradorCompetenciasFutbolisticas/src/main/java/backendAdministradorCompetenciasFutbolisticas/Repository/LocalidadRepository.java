package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    List<Localidad> findByOrderByProvinciaNombreAsc();

    List<Localidad> findByProvincia_IdOrderByNombreAsc(Long id);

    List<Localidad> findByProvincia_NombreOrderByNombre(String nombreProvincia);

    Optional<Localidad> findByNombre(String nombre);

    Optional<Localidad> findById(Long id);

    boolean existsByNombreAndAndProvincia_Nombre(String nombreLocalidad, String nombreProvincia);
}
