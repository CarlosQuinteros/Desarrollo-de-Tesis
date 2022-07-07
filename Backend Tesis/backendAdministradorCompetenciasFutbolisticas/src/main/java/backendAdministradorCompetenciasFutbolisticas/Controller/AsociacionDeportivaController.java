package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.AsociacionDeportivaDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InternalServerErrorException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.AsociacionDeportivaService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/asociaciones-deportivas")
@CrossOrigin("*")
public class AsociacionDeportivaController {

    @Autowired
    AsociacionDeportivaService asociacionDeportivaService;

    @Autowired
    LogService logService;

    @Autowired
    UsuarioService usuarioService;

    /*
        Metodo que permite craer una nueva Asociacion deportiva
     */
    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearAsociacion(@Valid @RequestBody AsociacionDeportivaDto asociacionDeportivaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        if(asociacionDeportivaService.existePorNombre(asociacionDeportivaDto.getNombre())){
            throw new BadRequestException("Ya existe una Asociacion con el nombre: " + asociacionDeportivaDto.getNombre());
        }
        if(asociacionDeportivaService.existePorEmail(asociacionDeportivaDto.getEmail())){
            throw new BadRequestException("Ya existe una Asociacion con el email: " + asociacionDeportivaDto.getEmail());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        AsociacionDeportiva asociacionDeportiva =  new AsociacionDeportiva(asociacionDeportivaDto.getNombre(), asociacionDeportivaDto.getAlias(), asociacionDeportivaDto.getEmail());
        try{
            boolean resultado = asociacionDeportivaService.guardarNuevaAsociacion(asociacionDeportiva);
            if(resultado){
                logService.guardarLogCreacionAsociacion(asociacionDeportiva, usuario);
                return new ResponseEntity<>(new Mensaje("Asociacion Deportiva guardada correctamente"),HttpStatus.OK);
            }
            throw new InternalServerErrorException("La asociacion no fue guardada correctamente");
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. Asociacion Deportiva no guardada");
        }
    }


    /*
        Metodo que permite actualizar una asociacion deportiva
     */
    @PutMapping("editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> editarAsociacion(@PathVariable ("id") Long id, @Valid @RequestBody AsociacionDeportivaDto asociacionDeportivaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        AsociacionDeportiva asociacionDeportivaActualizar = asociacionDeportivaService.getById(id);

        if(!asociacionDeportivaActualizar.getNombre().equals(asociacionDeportivaDto.getNombre()) && asociacionDeportivaService.existePorNombre(asociacionDeportivaDto.getNombre())){
            throw new BadRequestException("Ya existe una Asociacion Deportiva con el nombre: " + asociacionDeportivaDto.getNombre());
        }
        if(!asociacionDeportivaActualizar.getEmail().equals(asociacionDeportivaDto.getEmail()) && asociacionDeportivaService.existePorEmail(asociacionDeportivaDto.getEmail())){
            throw new BadRequestException("Ya existe una Asociacion con el email: " + asociacionDeportivaDto.getEmail());
        }
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.getUsuarioLogueado(auth);
            asociacionDeportivaActualizar.setNombre(asociacionDeportivaDto.getNombre());
            asociacionDeportivaActualizar.setAlias(asociacionDeportivaDto.getAlias());
            asociacionDeportivaActualizar.setEmail(asociacionDeportivaDto.getEmail());
            AsociacionDeportiva asociacionDeportivaEditada = asociacionDeportivaService.actualizarAsociacion(asociacionDeportivaActualizar);
            logService.guardarLogEdicionAsociacion(asociacionDeportivaEditada, usuario);
            return new ResponseEntity<>(new Mensaje("Asociacion Deportiva actualizada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. La Asociacion Deportiva no se actualizo");
        }
    }

    @GetMapping("/detalle/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<AsociacionDeportiva> detalleAsociacionDeportiva(@PathVariable("id") Long id){
        AsociacionDeportiva asociacionDeportiva = asociacionDeportivaService.getById(id);
        return new ResponseEntity<>(asociacionDeportiva, HttpStatus.OK);
    }


    /*
        Metodo que permite eliminar una asociacion deportiva
     */
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> eliminarAsociacion(@PathVariable ("id") Long id){
        AsociacionDeportiva asociacionDeportiva = asociacionDeportivaService.getById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.getUsuarioLogueado(auth);
        asociacionDeportivaService.eliminarAsociacion(asociacionDeportiva);
        logService.guardarLogEliminacionAsociacion(id, usuario);
        return new ResponseEntity<>(new Mensaje("Asociacion Deportiva eliminada correctamente"), HttpStatus.OK);
    }

    @GetMapping("/listado")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<AsociacionDeportiva>> listadoAsociaciones(){
        List<AsociacionDeportiva> listado = asociacionDeportivaService.getListadoOrdenadoPorNombre();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }
}
