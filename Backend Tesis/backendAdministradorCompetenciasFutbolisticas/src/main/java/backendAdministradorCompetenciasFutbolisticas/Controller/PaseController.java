package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.CambioDeClubDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Pase;
import backendAdministradorCompetenciasFutbolisticas.Service.PaseJugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/listado")
    public ResponseEntity<List<Pase>> listado(){
        List<Pase> listado = paseJugadorService.getListadoPases();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }

    @GetMapping("/cantidad")
    public ResponseEntity<Integer> cantidadPases(){
        Integer cantidad = paseJugadorService.cantidadPases();
        return new ResponseEntity<>(cantidad,HttpStatus.OK);
    }

    @PostMapping("/prueba")
    public ResponseEntity<?> prueba(@RequestBody CambioDeClubDto cambiio){
        System.out.println("lo que viene de js Date:  " + cambiio.getFecha());
        System.out.println("date de js a java to localdate: " + cambiio.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        return new ResponseEntity<>("todo parece ok", HttpStatus.OK);
    }


}
