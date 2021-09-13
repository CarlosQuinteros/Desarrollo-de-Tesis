package backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Dto.RecuperarPasswordDto;
import backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Dto.EmailValuesDto;
import backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Service.EmailService;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/existe-token/{tokenPassword}")
    public ResponseEntity<?> existeTokenPassword(@PathVariable ("tokenPassword") String tokenPassword){
        boolean existeToken = usuarioService.existByTokenPassword(tokenPassword);
        return existeToken ? new ResponseEntity(new Mensaje("Token de recuperación válido"), HttpStatus.OK) : new ResponseEntity(new Mensaje("Token de recuperación incorrecto"), HttpStatus.NOT_FOUND);
    }



    @GetMapping("/usuarioPorTokenPassword/{tokenPassword}")
    public  ResponseEntity<Usuario> getUsuarioByTokenPassword(@PathVariable ("tokenPassword") String tokenPassword){
        Optional<Usuario> usuarioOptional = usuarioService.getByTokenPassword(tokenPassword);
        if (!usuarioOptional.isPresent()){
            return new ResponseEntity(new Mensaje("No existe ningún usuario con el token indicado."), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        return new ResponseEntity(usuario, HttpStatus.OK);
    }

    @PostMapping("/enviar-email")
    public ResponseEntity<?> sendEmailPassword(@RequestBody EmailValuesDto dto){
        Optional<Usuario> usuarioOptional = usuarioService.getByNombreUsuarioOrEmail(dto.getMailTo());
        if (!usuarioOptional.isPresent()){
            return new ResponseEntity(new Mensaje("No existe ningún usuario con las credenciales ingresadas."), HttpStatus.NOT_FOUND);
        }
        try{
            Usuario usuario = usuarioOptional.get();
            dto.setMailTo(usuario.getEmail());
            dto.setUserName(usuario.getNombreUsuario());
            UUID uuid = UUID.randomUUID();
            String tokenPassword = uuid.toString();
            dto.setToken(tokenPassword);
            emailService.sendEmailRecuperarContraseña(dto);
            usuario.setTokenPassword(dto.getToken());

            usuarioService.save(usuario);
            return new ResponseEntity(new Mensaje("Te enviamos un correo"), HttpStatus.OK);

        }catch (Exception e){
            return  new ResponseEntity(new Mensaje("Error al enviar el correo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<?> cambiarPassword(@Valid @RequestBody RecuperarPasswordDto recuperarPasswordDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity(new Mensaje("Campos mal ingresados"), HttpStatus.BAD_REQUEST);
        }
        if(!recuperarPasswordDto.getPassword().equals(recuperarPasswordDto.getConfirmarPassword())){
            return new ResponseEntity(new Mensaje("Las contraseñas no coinciden"), HttpStatus.BAD_REQUEST);
        }
        Optional<Usuario> usuarioOptional = usuarioService.getByTokenPassword(recuperarPasswordDto.getTokenPassword());
        if(!usuarioOptional.isPresent()){
            return new ResponseEntity(new Mensaje("No existe ningún usuario con el token indicado"), HttpStatus.NOT_FOUND);
        }
        try {
            Usuario usuario = usuarioOptional.get();
            String nuevoPassword = passwordEncoder.encode(recuperarPasswordDto.getPassword());
            usuario.setPassword(nuevoPassword);
            usuario.setTokenPassword(null);

            usuarioService.save(usuario);
            return new ResponseEntity(new Mensaje("Contraseña actualizada correctamente"), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity(new Mensaje("Fallo la operación. La contraseña no se actualizó"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
