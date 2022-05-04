package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Dtos.NuevoJuezDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Juez;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.JuezService;
import backendAdministradorCompetenciasFutbolisticas.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/jueces")
@CrossOrigin("*")
public class JuezController {

    @Autowired
    private JuezService juezService;

    @Autowired
    private LogService logService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearNuevoJuez(@Valid @RequestBody NuevoJuezDto nuevoJuezDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        if(juezService.existeJuezPorDocumento(nuevoJuezDto.getDocumento())){
            throw new BadRequestException("Ya existe un Juez con el documento " + nuevoJuezDto.getDocumento());
        }
        if(juezService.existeJuezPorLegajo(nuevoJuezDto.getLegajo())){
            throw new BadRequestException("Ya existe un Juez con el legajo "+ nuevoJuezDto.getLegajo());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        Juez nuevoJuez = new Juez(nuevoJuezDto.getNombres(), nuevoJuezDto.getApellidos(), nuevoJuezDto.getDocumento(), nuevoJuezDto.getLegajo());
        juezService.guardarNuevoJuez(nuevoJuez);
        logService.guardarLogCreacionJuez(nuevoJuez,usuario);
        return new ResponseEntity<>(new Mensaje("Juez guardado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> editarJuez(@PathVariable ("id") Long id, @Valid @RequestBody NuevoJuezDto editarJuezDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Juez juezAEditar = juezService.getJuezPorId(id);
        if(!juezAEditar.getDocumento().equals(editarJuezDto.getDocumento()) && juezService.existeJuezPorDocumento(editarJuezDto.getDocumento())){
            throw new BadRequestException("Ya existe un Juez con el documento " + editarJuezDto.getDocumento());
        }
        if(!juezAEditar.getLegajo().equals(editarJuezDto.getLegajo()) && juezService.existeJuezPorLegajo(editarJuezDto.getLegajo())){
            throw new BadRequestException("Ya existe un Juez con el legajo " + editarJuezDto.getLegajo());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);

        juezAEditar.setNombres(editarJuezDto.getNombres());
        juezAEditar.setApellidos(editarJuezDto.getApellidos());
        juezAEditar.setDocumento(editarJuezDto.getDocumento());
        juezAEditar.setLegajo(editarJuezDto.getLegajo());
        Juez juezEditado = juezService.guardarJuez(juezAEditar);
        logService.guardarLogEdicionJuez(juezEditado, usuario);
        return new ResponseEntity<>(juezEditado, HttpStatus.OK);
    }

    //TODO: Si tiene referencias en partidos no debe eliminarse
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> eliminarJuez(@PathVariable("id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        Juez juezEliminar = juezService.getJuezPorId(id);
        juezService.eliminarJuez(id);
        logService.guardarLogEliminacionJuez(id, usuario);
        return new ResponseEntity<>(new Mensaje("Juez eliminado correctamente"), HttpStatus.OK);
    }

    @GetMapping("/listado")
    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<Juez>> getListadoJueces(){
        List listadoJueces = juezService.getListadoJueces();
        return new ResponseEntity<>(listadoJueces, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<Juez> getDetalleJuez(@PathVariable ("id") Long id){
        //el servicio genera exception si no obtiene el juez
        Juez juez = juezService.getJuezPorId(id);
        return  new ResponseEntity<>(juez, HttpStatus.OK);
    }

    @GetMapping("/detalle/dni-legajo/{dniOLegajo}")
    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<Juez> getDetalleJuezPorDocumentoOLegajo(@PathVariable ("dniOLegajo") String dniOLegajo){
        //el servicio genera exception si no obtiene el juez
        Juez juez = juezService.getJuezPorDocumentoOrLegajo(dniOLegajo);
        return new ResponseEntity<>(juez, HttpStatus.OK);
    }

    @GetMapping("/cantidad")
    @PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<Integer> getCantidadTotalJueces(){
        Integer cantidadJueces = juezService.getCantidadTotalJueces();
        return new ResponseEntity<>(cantidadJueces, HttpStatus.OK);
    }
}
