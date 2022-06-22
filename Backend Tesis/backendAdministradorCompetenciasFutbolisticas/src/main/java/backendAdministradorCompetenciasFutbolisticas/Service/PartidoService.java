package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PartidoService {

    @Autowired
    private PartidoRepository partidoRepository;

    public Partido guardarPartido(Partido partido){
        return partidoRepository.save(partido);
    }

    public Partido getDetallePartido(Long id){
        Partido partido = partidoRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("No existe el partido con ID: " + id));
        return partido;
    }

    public void eliminarPartido(Long id){
        Partido partido = getDetallePartido(id);
        partidoRepository.deleteById(id);
    }

    public List<Partido> historialEntreClubes(Long idClub1, Long idClub2){
        List<Partido> partidosEntreClubes = partidoRepository.findByClubLocal_IdAndClubVisitante_IdOrClubLocal_IdAndClubVisitante_Id(idClub1, idClub2, idClub2, idClub1);
        return partidosEntreClubes;
    }

    public boolean existePartidoPorId(Long id){
        return partidoRepository.existsById(id);
    }

    public void finalizarPartido(Long id){
        Partido partido = getDetallePartido(id);
        partido.cambiarEstadoAFinalizado();
    }


}
