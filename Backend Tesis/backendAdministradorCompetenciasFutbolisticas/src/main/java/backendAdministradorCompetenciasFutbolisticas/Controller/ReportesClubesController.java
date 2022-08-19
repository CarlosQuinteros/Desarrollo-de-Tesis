package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.DetalleGeneralPartidoDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoPartido;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reportes-clubes")
@CrossOrigin("*")
public class ReportesClubesController {

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private ClubService clubService;

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<DetalleGeneralPartidoDto>> historialEntreClubes(@RequestParam Long idClub1, @RequestParam Long idClub2){
        if(idClub1.equals(idClub2)){
            throw new BadRequestException("Los clubes no deben ser los mismos");
        }
        Club clubLocal = clubService.getClub(idClub1);
        Club clubVisitante = clubService.getClub(idClub2);
        List<DetalleGeneralPartidoDto> historial = partidoService.historialEntreClubes(clubLocal.getId(), clubVisitante.getId())
                .stream()
                .map(partido -> partidoService.mapPartidoToDetalleGeneralPartidoDto(partido))
                .collect(Collectors.toList());

        return new ResponseEntity<>(historial, HttpStatus.OK);
    }

    @GetMapping("/{id}/fixture")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<DetalleGeneralPartidoDto>> fixtureDeUnClub(@PathVariable Long id){
        List<DetalleGeneralPartidoDto> fixture = partidoService.fixtureDeUnClub(id).stream()
                .map(partido -> partidoService.mapPartidoToDetalleGeneralPartidoDto(partido))
                .collect(Collectors.toList());
        return new ResponseEntity<>(fixture, HttpStatus.OK);
    }

}
