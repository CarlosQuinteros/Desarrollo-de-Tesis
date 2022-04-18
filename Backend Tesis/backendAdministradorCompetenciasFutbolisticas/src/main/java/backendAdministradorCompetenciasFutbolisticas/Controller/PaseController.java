package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.CambioDeClubDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Service.PaseJugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pases")
@CrossOrigin("*")
public class PaseController {

    @Autowired
    PaseJugadorService paseJugadorService;

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/listado")
    public ResponseEntity<List<Pase>> listado(){
        List<Pase> listado = paseJugadorService.getListadoPases();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/cantidad")
    public ResponseEntity<Integer> cantidadPases(){
        Integer cantidad = paseJugadorService.cantidadPases();
        return new ResponseEntity<>(cantidad,HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_JUGADORES')")
    @GetMapping("/detalle/{id}")
    public  ResponseEntity<Pase> detallePase(@PathVariable ("id") Long id){
        //el servicio genera exception si no encuentra el pase
        Pase detallePase = paseJugadorService.getPaseById(id);
        return new ResponseEntity<Pase>(detallePase,HttpStatus.OK);
    }


}
