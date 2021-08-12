package backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Controller;

import backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Dto.EmailValuesDto;
import backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    EmailService emailService;

    @GetMapping("email/send")
    public ResponseEntity<?> sendEmail(){
        emailService.sendEmail();
        return new ResponseEntity("Correo enviado con exito", HttpStatus.OK);
    }
    @PostMapping("email/send-html")
    public ResponseEntity<?> sendEmailTemplate(@RequestBody EmailValuesDto dto){
        emailService.sendEmailConPlantilla(dto);
        return new ResponseEntity("Correo con plantilla enviado con exito", HttpStatus.OK);
    }
}
