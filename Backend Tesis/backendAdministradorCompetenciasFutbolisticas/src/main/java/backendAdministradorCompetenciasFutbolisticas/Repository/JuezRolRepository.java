package backendAdministradorCompetenciasFutbolisticas.Repository;

import backendAdministradorCompetenciasFutbolisticas.Entity.JuezRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuezRolRepository extends JpaRepository<JuezRol, Long> {

    boolean existsByJuez_IdAndPartido_Id(Long idJuez, Long idPartido);

    boolean existsByJuez_Id(Long idJuez);

    List<JuezRol> findByJuez_Id(Long idJuez);

    List<JuezRol> findByJuez_DocumentoOrJuez_Legajo(String documento, String legajo);

    List<JuezRol> findByPartido_Id(Long idPartido);
}
