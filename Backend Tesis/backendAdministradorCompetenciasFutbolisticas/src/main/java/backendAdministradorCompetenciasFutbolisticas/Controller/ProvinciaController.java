package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Dtos.ProvinciaDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Provincia;
import backendAdministradorCompetenciasFutbolisticas.Service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/provincias")
@CrossOrigin("*")
public class ProvinciaController {
    @Autowired
    ProvinciaService provinciaService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearProvincia(@Valid @RequestBody ProvinciaDto provinciaDto, BindingResult bindingResult){
        Optional<Provincia> provinciaOptional = provinciaService.getByNombre(provinciaDto.getNombre());
        if(bindingResult.hasErrors()){
            return new ResponseEntity(new Mensaje("Campos mal ingresados"), HttpStatus.NOT_FOUND);
        }
        if (provinciaService.existByNombre(provinciaDto.getNombre())){
            return new ResponseEntity(new Mensaje("La provincia ya existe"), HttpStatus.NOT_FOUND);
        }
        try{
            Provincia provincia = new Provincia(provinciaDto.getNombre());
            provinciaService.save(provincia);
            return new ResponseEntity(new Mensaje("Provincia guardada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity(new Mensaje("Fallo la operación. Provincia no guardada."), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/listado")
    public ResponseEntity<List<Provincia>> listadoProvincias(){
        List<Provincia> listado = provinciaService.getProvincias();
        return  new ResponseEntity(listado, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProvincia(@PathVariable ("id") Long id){
        Optional<Provincia> provinciaOptional = provinciaService.getById(id);
        if(!provinciaOptional.isPresent()){
            return  new ResponseEntity(new Mensaje("La provincia no existe"), HttpStatus.BAD_REQUEST);
        }
        Provincia provincia = provinciaOptional.get();
        if(!provincia.getLocalidades().isEmpty()){
            return new ResponseEntity(new Mensaje("La provincia contiene localidades y no puede eliminarse"), HttpStatus.BAD_REQUEST);
        }
        try{
            provinciaService.eliminarProvincia(id);
            return new ResponseEntity(new Mensaje("Provincia eliminada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(new Mensaje("Fallo la operacion. La provincia no se eliminó"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> detalleProvincia(@PathVariable ("id") Long id){
        Optional<Provincia> provinciaOptional = provinciaService.getById(id);
        if(!provinciaOptional.isPresent()){
            return new ResponseEntity(new Mensaje("La provincia no existe"), HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity(provinciaOptional.get(), HttpStatus.OK);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarProvincia (@PathVariable ("id") Long id, @Valid @RequestBody ProvinciaDto provinciaDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return  new ResponseEntity(new Mensaje("Campos mal ingresados"), HttpStatus.NOT_FOUND);
        }
        Optional<Provincia> provinciaOptional = provinciaService.getById(id);
        if(!provinciaOptional.isPresent()){
            return new ResponseEntity(new Mensaje("La provincia no existe"), HttpStatus.NOT_FOUND);
        }
        Provincia provincia = provinciaOptional.get();
        provincia.setNombre(provinciaDto.getNombre());
        try{
            provinciaService.save(provincia);
            return new ResponseEntity(new Mensaje("Provincia actualizada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return  new ResponseEntity(new Mensaje("Fallo la operacion. Provincia no actualizada"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
