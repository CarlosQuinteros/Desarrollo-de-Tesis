package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoPartido;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class PartidoService {

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private JuezRolService juezRolService;

    @Autowired
    private AnotacionService anotacionService;

    @Autowired
    private JugadorPartidoService jugadorPartidoService;

    public Partido guardarPartido(Partido partido) {
        if(partido.getClubVisitante().getId().equals(partido.getClubLocal().getId())){
            throw new BadRequestException("Para guardar un partido los clubes que se enfrentan no deben ser los mismos");
        }
        if(!partido.getJornada().getCompetencia().getClubesParticipantes().contains(partido.getClubLocal())){
            throw new BadRequestException("El club "+ partido.getClubLocal().getNombreClub() + " no forma parte de los clubes participantes de la competencia");
        }
        if(!partido.getJornada().getCompetencia().getClubesParticipantes().contains(partido.getClubVisitante())){
            throw new BadRequestException("El club "+ partido.getClubVisitante().getNombreClub() + " no forma parte de los clubes participantes de la competencia");
        }
        if(existeParticipacionDeClubEnJornada(partido.getJornada().getId(),partido.getClubLocal().getId())){
            throw new BadRequestException("El club " + partido.getClubLocal().getNombreClub() + " ya participa en un partido en la fecha ingresada");
        }
        if(existeParticipacionDeClubEnJornada(partido.getJornada().getId(),partido.getClubVisitante().getId())){
            throw new BadRequestException("El club " + partido.getClubVisitante().getNombreClub() + " ya participa en un partido en la fecha ingresada");
        }
        if(existePartidoConMismoClubLocalYClubVisitanteEnCompetencia(partido.getJornada().getCompetencia().getId(), partido.getClubLocal().getId(), partido.getClubVisitante().getId())){
            throw new BadRequestException("Ya existe un partido con el mismo club local y club visitante");
        }
        return partidoRepository.save(partido);
    }

    public Partido editarPartido(Long idPartido, Partido partidoEditado){
        Partido partidoEditar = getDetallePartido(idPartido);
        if(partidoEditar.getClubVisitante().getId().equals(partidoEditado.getClubVisitante().getId())
                && partidoEditar.getClubLocal().getId().equals(partidoEditado.getClubLocal().getId())

        ){
            partidoEditar.setFecha(partidoEditado.getFecha());
            partidoEditar.setObservaciones(partidoEditado.getObservaciones());
            return partidoRepository.save(partidoEditar);
        }
        partidoEditar.setFecha(partidoEditado.getFecha());
        partidoEditar.setClubLocal(partidoEditado.getClubLocal());
        partidoEditar.setClubVisitante(partidoEditado.getClubVisitante());
        partidoEditar.setObservaciones(partidoEditado.getObservaciones());
        return guardarPartido(partidoEditar);
    }

    public Partido getDetallePartido(Long id) {
        Partido partido = partidoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No existe el partido con ID: " + id));
        return partido;
    }

    public void eliminarPartido(Long id) {
        Partido partido = getDetallePartido(id);
        if(juezRolService.existeReferenciasConPartido(partido.getId())){
            throw new BadRequestException("El partido tiene referencias con jueces y no puede eliminarse");
        }
        if(jugadorPartidoService.existeReferenciasConPartido(partido.getId())){
            throw new BadRequestException("El partido tiene referencias con jugadores y no puede eliminarse");
        }
        partidoRepository.deleteById(id);
    }

    public List<DetalleGeneralPartidoDto> listadoDePartidosPorJornada(Long idJornada){
        List<DetalleGeneralPartidoDto> partidosPorJornada =
                partidoRepository.findByJornada_Id(idJornada)
                .stream()
                .map(partido -> mapPartidoToDetalleGeneralPartidoDto(partido))
                .collect(Collectors.toList());
        return partidosPorJornada;
    }

    public List<DetalleGeneralPartidoDto> listadoDePartidosPorCompetencia(Long idCompetencia){
        List<DetalleGeneralPartidoDto> partidosPorCompetencia =
                partidoRepository.findByJornada_Competencia_Id(idCompetencia)
                        .stream()
                        .map(partido -> mapPartidoToDetalleGeneralPartidoDto(partido))
                        .collect(Collectors.toList());
        return partidosPorCompetencia;
    }

    public List<Partido> historialEntreClubes(Long idClub1, Long idClub2) {
        List<Partido> partidosEntreClubes = partidoRepository.findByClubLocal_IdAndClubVisitante_IdOrClubLocal_IdAndClubVisitante_Id(idClub1, idClub2, idClub2, idClub1)
                .stream().filter(partido -> partido.getEstado().equals(NombreEstadoPartido.FINALIZADO))
                .collect(Collectors.toList());
        return partidosEntreClubes;
    }

    public boolean existePartidoPorId(Long id) {
        return partidoRepository.existsById(id);
    }

    public void finalizarPartido(Long id) {
        Partido partido = getDetallePartido(id);
        if(!jugadorPartidoService.existeReferenciasConPartido(id)){
            throw new BadRequestException("No se puede finalizar un partido sin participacion de jugadores");
        }
        partido.cambiarEstadoAFinalizado();
        partidoRepository.save(partido);
    }

    public void establecerPartidoComoPendiente(Long id){
        Partido partido = getDetallePartido(id);
        partido.cambiarEstadoAPendiente();
        partidoRepository.save(partido);
    }

    public boolean clubFormaParteDePartido(Partido partido, Club club) {
        return partidoRepository.existsByIdAndClubLocal_IdOrClubVisitante_Id(partido.getId(), club.getId(), club.getId());
    }

    public DetalleGeneralPartidoDto mapPartidoToDetalleGeneralPartidoDto(Partido partido){
        Integer cantidadGolesLocales = anotacionService.getListadoAnotacionesClubLocal(partido).size();
        Integer cantidadGolesVisitantes = anotacionService.getListadoAnotacionesClubVisitante(partido).size();

        DetalleGeneralPartidoDto informacionGeneral = new DetalleGeneralPartidoDto(
                partido.getId(),
                partido.getFecha(),
                partido.getObservaciones(),
                partido.getEstado().name(),
                partido.getClubLocal().getNombreClub(),
                partido.getClubVisitante().getNombreClub(),
                cantidadGolesLocales,
                cantidadGolesVisitantes,
                partido.getJornada().getCompetencia().getNombre(),
                partido.getJornada().getDescripcion(),
                partido.getJornada().getCompetencia().getCategoria().getNombre()
        );
        return  informacionGeneral;
    }

    public boolean existeParticipacionDeClubEnJornada(Long idJornada, Long idClub){
        return partidoRepository.existsByJornada_IdAndClubLocal_IdOrClubVisitante_Id(idJornada,idClub,idClub);
    }

    public boolean existePartidoConMismoClubLocalYClubVisitanteEnCompetencia(Long idCompetencia, Long idClubLocal, Long idClubVisitante){
        return partidoRepository.existsByJornada_Competencia_IdAndAndClubLocal_IdAndClubVisitante_Id(idCompetencia, idClubLocal, idClubVisitante);
    }

    public boolean existeReferenciasConJornada(Long idJornada){
        return partidoRepository.existsByJornada_Id(idJornada);
    }

    public boolean existeReferenciasConClub(Long idClub){
        return partidoRepository.existsByClubLocal_IdOrClubVisitante_Id(idClub, idClub);
    }



}
