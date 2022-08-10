package backendAdministradorCompetenciasFutbolisticas.Controller;


import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.EstadisticasGeneralesCompetenciaDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Competencia;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoPartido;
import backendAdministradorCompetenciasFutbolisticas.Service.CompetenciaService;
import backendAdministradorCompetenciasFutbolisticas.Service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reportes-competencia")
@CrossOrigin("*")
public class ReportesCompetenciaController {

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private CompetenciaService competenciaService;

    @GetMapping("/{id}/generales")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<EstadisticasGeneralesCompetenciaDto> estadisticasGenerales(@PathVariable Long id){
        Integer golesLocales = 0;
        Integer golesVisitantes = 0;
        Integer victoriasLocales = 0;
        Integer victoriasVisitantes = 0;
        Integer empates = 0;
        Double media;
        Competencia competencia = competenciaService.getCompetencia(id);
        EstadisticasGeneralesCompetenciaDto estadisticasGenerales = new EstadisticasGeneralesCompetenciaDto();
        List<DetalleGeneralPartidoDto> partidos = partidoService.listadoDePartidosPorCompetencia(competencia.getId()).stream().filter(p -> p.getEstado().equals(NombreEstadoPartido.FINALIZADO.name())).collect(Collectors.toList());
        for (DetalleGeneralPartidoDto partido : partidos) {
            golesLocales += partido.getCantidadGolesClubLocal();
            golesVisitantes += partido.getCantidadGolesClubVisitante();
            if(partido.getCantidadGolesClubLocal() > partido.getCantidadGolesClubVisitante()){
                victoriasLocales ++;
            }
            if(partido.getCantidadGolesClubVisitante() > partido.getCantidadGolesClubLocal()){
                victoriasVisitantes++;
            }
            if(partido.getCantidadGolesClubLocal() == partido.getCantidadGolesClubVisitante()){
                empates++;
            }
        }
        media = (partidos.size() == 0) ? Double.valueOf(0) : (double) (golesLocales + golesVisitantes) / partidos.size();
        estadisticasGenerales.setCantidadGolesLocales(golesLocales);
        estadisticasGenerales.setCantidadGolesVisitantes(golesVisitantes);
        estadisticasGenerales.setVictoriasLocales(victoriasLocales);
        estadisticasGenerales.setVictoriasVisitantes(victoriasVisitantes);
        estadisticasGenerales.setEmpates(empates);
        estadisticasGenerales.setMediaGoles(media);

        return new ResponseEntity<>(estadisticasGenerales, HttpStatus.OK);

    }


}
