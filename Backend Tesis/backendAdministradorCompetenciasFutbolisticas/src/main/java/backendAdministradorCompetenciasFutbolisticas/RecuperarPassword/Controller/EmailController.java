package backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Dto.EmailValuesDto;
import backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Service.EmailService;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email-password")
@CrossOrigin("*")
public class EmailController {
    @Autowired
    EmailService emailService;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/enviar-email")
    public ResponseEntity<?> sendEmailPassword(@RequestBody EmailValuesDto dto){
        Optional<Usuario> usuarioOptional = usuarioService.getByNombreUsuarioOrEmail(dto.getMailTo());
        if (!usuarioOptional.isPresent()){
            return new ResponseEntity(new Mensaje("No existe ningún usuario con las credenciales ingresadas."), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        dto.setMailTo(usuario.getEmail());
        dto.setUserName(usuario.getNombreUsuario());
        UUID uuid = UUID.randomUUID();
        String tokenPassword = uuid.toString();
        dto.setToken(tokenPassword);
        emailService.sendEmailRecuperarContraseña(dto);
        usuario.setTokenPassword(dto.getToken());
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("te enviamos un correo."), HttpStatus.OK);
    }
}
