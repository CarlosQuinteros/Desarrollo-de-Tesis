package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.LocalidadDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import backendAdministradorCompetenciasFutbolisticas.Entity.Provincia;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.LocalidadNoExisteException;
import backendAdministradorCompetenciasFutbolisticas.Repository.LocalidadRepository;
import backendAdministradorCompetenciasFutbolisticas.Repository.ProvinciaRepository;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.LocalidadService;
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

    @Autowired
    ClubService clubService;


    /*
        Metodo que permite crear una nueva localidad
    */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearLocalidad(@Valid @RequestBody LocalidadDto localidadDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("Campos mal ingresados"), HttpStatus.NOT_FOUND);
        }
        Optional<Provincia> provinciaOptional = provinciaService.getByNombre(localidadDto.getProvincia());
        if(!provinciaOptional.isPresent()){
            return  new ResponseEntity<>(new Mensaje("La provincia no existe"), HttpStatus.NOT_FOUND);
        }
        if(localidadService.existeLocalidadNombreYProvinciaNombre(localidadDto.getNombre(), localidadDto.getProvincia())){
            return new ResponseEntity<>(new Mensaje("La Provincia de " + localidadDto.getProvincia() + " ya contiene esa localidad"), HttpStatus.NOT_FOUND);
        }
        Provincia provincia = provinciaOptional.get();
        Localidad localidad = new Localidad(localidadDto.getNombre(),provincia);
        try{

            if(localidadService.save(localidad)){
                return new ResponseEntity<>(new Mensaje("Localidad guardada correctamente"), HttpStatus.OK);
            }
            return  new ResponseEntity<>(new Mensaje("Localidad no guardada correctamente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. Localidad no guardada"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
        Metodo que retorna el listado de localidades
    */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listado")
    public ResponseEntity<List<Localidad>> listadoLocalidades(){
        List<Localidad> listaLocalidades = localidadService.getListado();
            return new ResponseEntity<>(listaLocalidades, HttpStatus.OK);
    }


    /*
        Metodo que retorna el listado de localidades de una cierta provincia
    */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listado/{idProvincia}")
    public ResponseEntity<List<Localidad>> listadoLocalidadPorIdProvincia(@PathVariable("idProvincia") Long idProvincia){
        List<Localidad> listaLocalidades = localidadService.getLocalidadesPorIdProvincia(idProvincia);
        return  new ResponseEntity<>(listaLocalidades, HttpStatus.OK);
    }


    /*
        Metodo que permite eliminar una localidad, solo si no tiene clubes asociados o jugadores (TODO)
    */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarLocalidad(@PathVariable ("id") Long id){
        if(!localidadService.existById(id)){
            return new ResponseEntity<>(new Mensaje("La localidad no existe"), HttpStatus.NOT_FOUND);
        }
        try{
            Localidad localidad = localidadService.getById(id).get();
            if(clubService.existeClubPorAsociacion(localidad.getId())){
                return new ResponseEntity<>(new Mensaje("La localidad tiene clubes asociados y no puede eliminarse"), HttpStatus.BAD_REQUEST);
            }
            Provincia provincia = provinciaService.getById(localidad.getProvincia().getId()).get();
            provincia.eliminarLocalidad(localidad);
            localidadService.eliminarLocalidad(id);
            return new ResponseEntity<>(new Mensaje("Localidad eliminada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. La localidad no se elimino"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
        Metodo que permite editar una localidad
    */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarLocalidad(@PathVariable ("id") Long id, @Valid @RequestBody LocalidadDto localidadDto, BindingResult bindingResult){
        try{
        Localidad localidadActualizar = localidadService.getLocalidadPorID(id);
        Optional<Provincia> provinciaOptional = provinciaService.getByNombre(localidadDto.getProvincia());
        if(!provinciaOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("La provincia no existe"), HttpStatus.NOT_FOUND);
        }
        if(localidadService.existeLocalidadNombreYProvinciaNombre(localidadDto.getNombre(), localidadDto.getProvincia())){
            return new ResponseEntity<>(new Mensaje("La Provincia de " + localidadDto.getProvincia() + " ya contiene esa localidad"), HttpStatus.NOT_FOUND);
        }
        Provincia provinciaActualizar = provinciaOptional.get();
            boolean resultado = localidadService.actualizar(localidadActualizar, localidadDto, provinciaActualizar);
            if(resultado){
                return new ResponseEntity<>(new Mensaje("Localidad actualizada correctamente"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new Mensaje("Localidad no actualizada correctamente"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (LocalidadNoExisteException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. La localidad no se actualizo"), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    /*
        Metodo que retorna una localidad por su id. Si se captura LocalidadNoExisteExceptcion, se retorna el mensaje
        establecido por el metodo getLocalidadPorId de localidadService
    */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> getDetalleLocalidad(@PathVariable ("id") Long id) {
        try{
            Localidad localidad = localidadService.getLocalidadPorID(id);
            return new ResponseEntity<>(localidad, HttpStatus.OK);
        }catch (LocalidadNoExisteException e){
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


}
