package backendAdministradorCompetenciasFutbolisticas.Security.Repository;

import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByRolNombre(String nombreRol);

}
