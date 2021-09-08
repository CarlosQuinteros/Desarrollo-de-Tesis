package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.LocalidadDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import backendAdministradorCompetenciasFutbolisticas.Entity.Provincia;
import backendAdministradorCompetenciasFutbolisticas.Repository.LocalidadRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.ProvinciaRepository;
import backendAdministradorCompetenciasFutbolisticas.Service.LocalidadService;
import backendAdministradorCompetenciasFutbolisticas.Service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.servlet.OAuth2ResourceServerDsl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/localidades")
@CrossOrigin("*")
public class LocalidadController {

    @Autowired
    LocalidadRepository localidadRepository;

    @Autowired
    ProvinciaRepository provinciaRepository;

    @Autowired
    LocalidadService localidadService;

    @Autowired
    ProvinciaService provinciaService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearLocalidad(@Valid @RequestBody LocalidadDto localidadDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity(new Mensaje("Campos mal ingresados"), HttpStatus.NOT_FOUND);
        }
        Optional<Provincia> provinciaOptional = provinciaService.getByNombre(localidadDto.getProvincia());
        if(!provinciaOptional.isPresent()){
            return  new ResponseEntity(new Mensaje("La provincia no existe"), HttpStatus.NOT_FOUND);
        }
        if(localidadService.existeLocalidadNombreYProvinciaNombre(localidadDto.getNombre(), localidadDto.getProvincia())){
            return new ResponseEntity(new Mensaje("La Provincia de " + localidadDto.getProvincia() + " ya contiene esa localidad"), HttpStatus.NOT_FOUND);
        }
        Provincia provincia = provinciaOptional.get();
        Localidad localidad = new Localidad(localidadDto.getNombre(),provincia);
        try{

            localidadService.save(localidad);
            return new ResponseEntity(new Mensaje("Localidad guardada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(new Mensaje("Fallo la operacion. Localidad no guardada"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listado")
        public ResponseEntity<List<Localidad>> listadoLocalidades(){
        List<Localidad> listaLocalidades = localidadService.getListado();
            return new ResponseEntity(listaLocalidades, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarLocalidad(@PathVariable ("id") Long id){
        if(!localidadService.existById(id)){
            return new ResponseEntity(new Mensaje("La localidad no existe"), HttpStatus.NOT_FOUND);
        }
        try{
            Localidad localidad = localidadService.getById(id).get();
            Provincia provincia = provinciaService.getById(localidad.getProvincia().getId()).get();
            provincia.eliminarLocalidad(localidad);
            localidadService.eliminarLocalidad(id);
            for (Localidad l : provincia.getLocalidades()  ) {
                System.out.println(l.getNombre() + " : " + l.getProvincia().getNombre() );
            }
            return new ResponseEntity(new Mensaje("Localidad eliminada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(new Mensaje("Fallo la operacion. La localidad no se elimino"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarLocalidad(@PathVariable ("id") Long id, @Valid @RequestBody LocalidadDto localidadDto, BindingResult bindingResult){
        Optional<Localidad> localidadOptional = localidadService.getById(id);
        Optional<Provincia> provinciaOptional = provinciaService.getByNombre(localidadDto.getProvincia());
        if(!localidadOptional.isPresent()){
            return new ResponseEntity(new Mensaje("La localidad no existe"), HttpStatus.NOT_FOUND);
        }
        if(!provinciaOptional.isPresent()){
            return new ResponseEntity(new Mensaje("La provincia no existe"), HttpStatus.NOT_FOUND);
        }
        if(localidadService.existeLocalidadNombreYProvinciaNombre(localidadDto.getNombre(), localidadDto.getProvincia())){
            return new ResponseEntity(new Mensaje("La Provincia de " + localidadDto.getProvincia() + " ya contiene esa localidad"), HttpStatus.NOT_FOUND);
        }
        Localidad localidadActualizar = localidadOptional.get();
        Provincia provinciaActualizar = provinciaOptional.get();
        localidadActualizar.setNombre(localidadDto.getNombre());
        localidadActualizar.setProvincia(provinciaActualizar);
        try{

            localidadService.save(localidadActualizar);
            for (Localidad localidad : provinciaActualizar.getLocalidades()  ) {
                System.out.println(localidad.getNombre() + " : " + localidad.getProvincia().getNombre() );
            }
            return new ResponseEntity(new Mensaje("Localidad actualizada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(new Mensaje("Fallo la operacion. La localidad no se actualizo"), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


}
