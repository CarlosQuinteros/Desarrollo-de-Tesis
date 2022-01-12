package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByUsuario_IdOrderByFechaAsc(Long id);

    List<Log> findByUsuario_IdOrderByFechaDesc(Long id);

    Integer countByUsuario_Id(Long id);
}
