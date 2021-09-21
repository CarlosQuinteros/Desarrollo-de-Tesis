package backendAdministradorCompetenciasFutbolisticas.Security.Repository;

import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findAllByOrderByApellidoAsc();
    Optional <Usuario> findByNombreUsuario(String nombreUsuario);
    Optional <Usuario> findByNombreUsuarioOrEmail(String nombreUsuario, String email);
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    boolean existsByNombreUsuarioAndActivoIsTrue(String nombreUsuario);
    boolean existsByTokenPassword(String tokenPassword);
    int countUsuarioByActivoTrue();
    int countUsuarioByActivoFalse();
    Optional <Usuario> findByTokenPassword(String tokenPassword);

}
