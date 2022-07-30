package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.JuezRolDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Juez;
import backendAdministradorCompetenciasFutbolisticas.Entity.JuezRol;
import backendAdministradorCompetenciasFutbolisticas.Entity.Partido;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreRolJuez;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Service.JuezRolService;
import backendAdministradorCompetenciasFutbolisticas.Service.JuezService;
import backendAdministradorCompetenciasFutbolisticas.Service.PartidoService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/participaciones-jueces")
@CrossOrigin("*")
public class JuezRolController {

    @Autowired
    private JuezRolService juezRolService;

    @Autowired
    private JuezService juezService;

    @Autowired
    private PartidoService partidoService;

    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearParticipacionDeJuezEnPartido(@Valid @RequestBody JuezRolDto nuevoJuezEnPartido, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        NombreRolJuez rolJuez = juezRolService.getNombreRolJuezPorNombre(nuevoJuezEnPartido.getRol());
        Juez juez = juezService.getJuezPorId(nuevoJuezEnPartido.getIdJuez());
        Partido partido = partidoService.getDetallePartido(nuevoJuezEnPartido.getIdPartido());

        JuezRol nuevoJuezRol = new JuezRol(juez,rolJuez, partido);
        juezRolService.crearJuezRol(nuevoJuezRol);
        return new ResponseEntity<>(new Mensaje("Participacion de juez guardada correctamente"), HttpStatus.CREATED);
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> editarParticipacionDeJuezEnPartido(@PathVariable ("id") Long id, @Valid @RequestBody JuezRolDto juezRolDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        JuezRol juezRol = juezRolService.getDetalleJuezRolPorId(id);
        Juez juez = juezService.getJuezPorId(juezRolDto.getIdJuez());
        NombreRolJuez rolJuez = juezRolService.getNombreRolJuezPorNombre(juezRolDto.getRol());

        if(!juezRol.getJuez().getId().equals(juez.getId())
                && juezRolService.existeParticipacionDeJuezEnPartido(juez.getId(),juezRol.getPartido().getId())){
            throw new BadRequestException("El juez solo puede tener un rol en un partido");
        }
        if(rolJuez.equals(NombreRolJuez.ARBITRO_PRINCIPAL) && juezRolService.existeArbitroPrincipalEnPartido(juezRol.getPartido().getId())){
            throw new BadRequestException("Ya existe el arbitro principal");
        }
        juezRol.setJuez(juez);
        juezRol.setRol(rolJuez);
        juezRol = juezRolService.guardarJuezRol(juezRol);
        return new ResponseEntity<>(new Mensaje("Participacion de juez guardada correctamente"), HttpStatus.OK);

    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> eliminarParticipacionDeJuezEnPartido(@PathVariable("id") Long id){
        JuezRol juezRol = juezRolService.getDetalleJuezRolPorId(id);
        juezRolService.eliminarJuezRol(id);
        return new ResponseEntity<>(new Mensaje("Participacion de juez eliminada correctamente"),HttpStatus.OK);
    }



}
