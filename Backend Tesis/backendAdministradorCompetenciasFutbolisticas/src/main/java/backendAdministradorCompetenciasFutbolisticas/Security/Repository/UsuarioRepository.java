package backendAdministradorCompetenciasFutbolisticas.Security.Repository;

import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional <Usuario> findByNombreUsuario(String nmombreUsuario);
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    boolean existsByNombreUsuarioAndActivoIsTrue(String nombreUsuario);
}
