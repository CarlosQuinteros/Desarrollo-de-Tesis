package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.AsociacionDeportivaDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
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
            bindingResult.getAllErrors().forEach(objectError -> objectError.getDefaultMessage());
            return new ResponseEntity<>(new Mensaje(bindingResult.getFieldError().getDefaultMessage()), HttpStatus.NOT_FOUND);
        }
        if(asociacionDeportivaService.existePorNombre(asociacionDeportivaDto.getNombre())){
            return new ResponseEntity<>(new Mensaje("La Asociacion Deportiva ingresada ya existe"), HttpStatus.BAD_REQUEST);
        }
        AsociacionDeportiva asociacionDeportiva =  new AsociacionDeportiva(asociacionDeportivaDto.getNombre());
        try{
            boolean resultado = asociacionDeportivaService.guardarNuevaAsociacion(asociacionDeportiva);
            if(resultado){
                return new ResponseEntity<>(new Mensaje("Asociacion Deportiva guardada correctamente"),HttpStatus.OK);
            }
            return  new ResponseEntity<>(new Mensaje("Asociacion Deportiva no guardada correctamente"),HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. Asociacion Deportiva no guardada"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
        Metodo que permite actualizar una asociacion deportiva
     */
    @PutMapping("editar/{id}")
    public ResponseEntity<?> editarAsociacion(@PathVariable ("id") Long id, @Valid @RequestBody AsociacionDeportivaDto asociacionDeportivaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("Campos mal ingresados"), HttpStatus.NOT_FOUND);
        }
        Optional<AsociacionDeportiva> asociacionDeportivaOptional = asociacionDeportivaService.getById(id);

        if(!asociacionDeportivaOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("No existe la Asociacion Deportiva a editar"), HttpStatus.BAD_REQUEST);
        }
        AsociacionDeportiva asociacionDeportivaActualizar = asociacionDeportivaOptional.get();
        if(asociacionDeportivaActualizar.getNombre()!= asociacionDeportivaDto.getNombre() && asociacionDeportivaService.existePorNombre(asociacionDeportivaDto.getNombre())){
            return new ResponseEntity<>(new Mensaje("Ya existe una Asociacion Deportiva con el nombre ingresado"), HttpStatus.BAD_REQUEST);
        }
        try{
            asociacionDeportivaActualizar.setNombre(asociacionDeportivaDto.getNombre());
            asociacionDeportivaService.actualizarAsociacion(asociacionDeportivaActualizar);
            return new ResponseEntity<>(new Mensaje("Asociacion Deportiva actualizada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. La Asociacion Deportiva no se actualizo"),HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(new Mensaje("La Asociacion Deportiva a eliminar no existe"), HttpStatus.NOT_FOUND);
        }
        AsociacionDeportiva asociacionDeportiva = asociacionDeportivaOptional.get();
        try{
            asociacionDeportivaService.eliminarAsociacion(asociacionDeportiva);
            return new ResponseEntity<>(new Mensaje("Asociacion Deportiva eliminada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. La Asociacion Deportiva no se elimino"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listado")
    public ResponseEntity<List<AsociacionDeportiva>> listadoAsociaciones(){
        List<AsociacionDeportiva> listado = asociacionDeportivaService.getListadoOrdenadoPorNombre();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }
}
