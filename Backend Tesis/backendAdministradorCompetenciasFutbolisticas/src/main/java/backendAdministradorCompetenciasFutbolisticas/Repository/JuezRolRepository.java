package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.JuezRol;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreRolJuez;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuezRolRepository extends JpaRepository<JuezRol, Long> {

    boolean existsByJuez_IdAndPartido_Id(Long idJuez, Long idPartido);

    boolean existsByJuez_Id(Long idJuez);

    boolean existsByPartido_IdAndRol(Long idPartido, NombreRolJuez rol);

    List<JuezRol> findByJuez_Id(Long idJuez);

    List<JuezRol> findByJuez_DocumentoOrJuez_Legajo(String documento, String legajo);

    List<JuezRol> findByPartido_Id(Long idPartido);
}
