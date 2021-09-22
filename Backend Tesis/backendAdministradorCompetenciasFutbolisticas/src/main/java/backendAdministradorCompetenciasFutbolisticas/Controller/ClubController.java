package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.ClubDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.AsociacionDeportiva;
import backendAdministradorCompetenciasFutbolisticas.Entity.Club;
import backendAdministradorCompetenciasFutbolisticas.Entity.Localidad;
import backendAdministradorCompetenciasFutbolisticas.Entity.Responsable;
import backendAdministradorCompetenciasFutbolisticas.Security.Enums.RolNombre;
import backendAdministradorCompetenciasFutbolisticas.Service.AsociacionDeportivaService;
import backendAdministradorCompetenciasFutbolisticas.Service.ClubService;
import backendAdministradorCompetenciasFutbolisticas.Service.LocalidadService;
import backendAdministradorCompetenciasFutbolisticas.Service.ResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/clubes")
@CrossOrigin("*")
public class ClubController {


    @Autowired
    ClubService clubService;

    @Autowired
    LocalidadService localidadService;

    @Autowired
    AsociacionDeportivaService asociacionDeportivaService;

    @Autowired
    ResponsableService responsableService;

    /*
        Metodo que permite crear un nuevo club
     */

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearClub(@Valid @RequestBody ClubDto clubDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("Campos mal ingresados"), HttpStatus.NOT_FOUND);
        }
        Optional<AsociacionDeportiva> asociacionDeportivaOptional = asociacionDeportivaService.getById(clubDto.getAsociacionDeportiva());
        Optional<Localidad> localidadOptional = localidadService.getById(clubDto.getIdLocalidad());
        if(!localidadOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("La Localidad no existe"), HttpStatus.NOT_FOUND);
        }
        if(!asociacionDeportivaOptional.isPresent()){
            return new ResponseEntity<>(new Mensaje("La Asociacion deportiva no existe"), HttpStatus.NOT_FOUND);
        }
        if(clubService.existeClubNombreYAsociacionNombre(clubDto.getNombre(),asociacionDeportivaOptional.get().getNombre())){
            return new ResponseEntity<>(new Mensaje("La Asociacion Deportiva ingresada ya contiene el club " + clubDto.getNombre()), HttpStatus.BAD_REQUEST);
        }
        if(clubService.existByEmail(clubDto.getEmail())){
            return new ResponseEntity<>(new Mensaje("Ya existe un club con el email ingresado"), HttpStatus.BAD_REQUEST);
        }
        if(responsableService.existeResponsablePorDocumento(clubDto.getResponsable().getDocumento())){
            return new ResponseEntity<>(new Mensaje("Ya existe un responsable con el documento ingresado"), HttpStatus.BAD_REQUEST);
        }
        if(responsableService.existeResponsablePorEmail(clubDto.getResponsable().getEmail())){
            return new ResponseEntity<>(new Mensaje("Ya existe un responsable con el email ingresado"), HttpStatus.BAD_REQUEST);

        }
        try {
            Responsable nuevoResponsable = new Responsable(clubDto.getResponsable().getNombre(),clubDto.getResponsable().getApellido(), clubDto.getResponsable().getDocumento(), clubDto.getResponsable().getEmail());
            Club nuevoClub = new Club(clubDto.getNombre(), clubDto.getFechaFundacion(), clubDto.getEmail(), localidadOptional.get(),nuevoResponsable,asociacionDeportivaOptional.get());
            boolean resultadoCarga = clubService.guardarNuevoClub(nuevoClub);
            if(resultadoCarga){
                return new ResponseEntity<>(new Mensaje("Club guardado correctamente"), HttpStatus.CREATED);
            }else return  new ResponseEntity<>(new Mensaje("Club no guardado correctamente"), HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion. No se guardo el nuevo club"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
