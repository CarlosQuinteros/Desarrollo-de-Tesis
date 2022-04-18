package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.AsociacionDeportivaDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InternalServerErrorException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Service.AsociacionDeportivaService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/asociacionesDeportivas")
@CrossOrigin("*")
public class AsociacionDeportivaController {

    @Autowired
    AsociacionDeportivaService asociacionDeportivaService;

    @Autowired
    ClubService clubService;

    /*
        Metodo que permite craer una nueva Asociacion deportiva
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearAsociacion(@Valid @RequestBody AsociacionDeportivaDto asociacionDeportivaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        if(asociacionDeportivaService.existePorNombre(asociacionDeportivaDto.getNombre())){
            throw new BadRequestException("La Asociacion Deportiva ingresada ya existe");
        }
        AsociacionDeportiva asociacionDeportiva =  new AsociacionDeportiva(asociacionDeportivaDto.getNombre());
        try{
            boolean resultado = asociacionDeportivaService.guardarNuevaAsociacion(asociacionDeportiva);
            if(resultado){
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
    public ResponseEntity<?> editarAsociacion(@PathVariable ("id") Long id, @Valid @RequestBody AsociacionDeportivaDto asociacionDeportivaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Optional<AsociacionDeportiva> asociacionDeportivaOptional = asociacionDeportivaService.getById(id);

        if(!asociacionDeportivaOptional.isPresent()){
            throw new ResourceNotFoundException("La Asociacion Deportiva ID: " + id + " no existe");
        }
        AsociacionDeportiva asociacionDeportivaActualizar = asociacionDeportivaOptional.get();
        if(asociacionDeportivaActualizar.getNombre()!= asociacionDeportivaDto.getNombre() && asociacionDeportivaService.existePorNombre(asociacionDeportivaDto.getNombre())){
            throw new BadRequestException("Ya existe una Asociacion Deportiva con el nombre ingresado");
        }
        try{
            asociacionDeportivaActualizar.setNombre(asociacionDeportivaDto.getNombre());
            asociacionDeportivaService.actualizarAsociacion(asociacionDeportivaActualizar);
            return new ResponseEntity<>(new Mensaje("Asociacion Deportiva actualizada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. La Asociacion Deportiva no se actualizo");
        }
    }


    /*
        Metodo que permite eliminar una asociacion deportiva
        TODO: NO SE PUEDE ELIMINAR SI TIENE REFERENCIAS EN TORNEOS.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarAsociacion(@PathVariable ("id") Long id){
        Optional<AsociacionDeportiva> asociacionDeportivaOptional = asociacionDeportivaService.getById(id);
        if(!asociacionDeportivaOptional.isPresent()){
            throw new ResourceNotFoundException("La Asociacion Deportiva ID: " + id + " no existe");
        }
        AsociacionDeportiva asociacionDeportiva = asociacionDeportivaOptional.get();
        try{
            asociacionDeportivaService.eliminarAsociacion(asociacionDeportiva);
            return new ResponseEntity<>(new Mensaje("Asociacion Deportiva eliminada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. La Asociacion Deportiva no se elimino");
        }
    }

    @GetMapping("/listado")
    public ResponseEntity<List<AsociacionDeportiva>> listadoAsociaciones(){
        List<AsociacionDeportiva> listado = asociacionDeportivaService.getListadoOrdenadoPorNombre();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }
}
