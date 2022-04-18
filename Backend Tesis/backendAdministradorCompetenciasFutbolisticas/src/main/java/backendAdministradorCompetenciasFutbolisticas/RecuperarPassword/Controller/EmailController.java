package backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InternalServerErrorException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
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
        if(!usuarioService.existByTokenPassword(tokenPassword)){
            throw new ResourceNotFoundException("Token de recuperacion incorrecto");
        }
        return new ResponseEntity(new Mensaje("Token de recuperación válido"), HttpStatus.OK);
    }



    @GetMapping("/usuarioPorTokenPassword/{tokenPassword}")
    public  ResponseEntity<Usuario> getUsuarioByTokenPassword(@PathVariable ("tokenPassword") String tokenPassword){
        Optional<Usuario> usuarioOptional = usuarioService.getByTokenPassword(tokenPassword);
        if (!usuarioOptional.isPresent()){
            throw new ResourceNotFoundException("No existe ningún usuario con el token indicado");
        }
        Usuario usuario = usuarioOptional.get();
        return new ResponseEntity(usuario, HttpStatus.OK);
    }

    @PostMapping("/enviar-email")
    public ResponseEntity<?> sendEmailPassword(@RequestBody EmailValuesDto dto){
        Optional<Usuario> usuarioOptional = usuarioService.getByNombreUsuarioOrEmail(dto.getMailTo());
        if (!usuarioOptional.isPresent()){
            throw new ResourceNotFoundException("No existe el usuario: " + dto.getMailTo());
        }
        try{
            Usuario usuario = usuarioOptional.get();
            dto.setMailTo(usuario.getEmail());
            dto.setUserName(usuario.getNombreUsuario());
            UUID uuid = UUID.randomUUID();
            String tokenPassword = uuid.toString();
            dto.setToken(tokenPassword);
            emailService.sendEmailRecuperarPassword(dto);
            usuario.setTokenPassword(dto.getToken());

            usuarioService.save(usuario);
            return new ResponseEntity(new Mensaje("Te enviamos un correo"), HttpStatus.OK);

        }catch (Exception e){
            throw new InternalServerErrorException("Error al enviar el correo");
        }

    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<?> cambiarPassword(@Valid @RequestBody RecuperarPasswordDto recuperarPasswordDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        if(!recuperarPasswordDto.getPassword().equals(recuperarPasswordDto.getConfirmarPassword())){
            throw new BadRequestException("Las contraseñas no coinciden");
        }
        Optional<Usuario> usuarioOptional = usuarioService.getByTokenPassword(recuperarPasswordDto.getTokenPassword());
        if(!usuarioOptional.isPresent()){
            throw new ResourceNotFoundException("No existe un usuario con el token ingresado");
        }
        try {
            Usuario usuario = usuarioOptional.get();
            String nuevoPassword = passwordEncoder.encode(recuperarPasswordDto.getPassword());
            usuario.setPassword(nuevoPassword);
            usuario.setTokenPassword(null);

            usuarioService.save(usuario);
            return new ResponseEntity(new Mensaje("Contraseña actualizada correctamente"), HttpStatus.OK);

        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operación. La contraseña no se actualizó");
        }

    }
}
