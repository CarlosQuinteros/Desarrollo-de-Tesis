package backendAdministradorCompetenciasFutbolisticas.Security.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.UnauthorizedException;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.JwtDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.LoginUsuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.NuevoUsuarioDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Rol;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Enums.RolNombre;
import backendAdministradorCompetenciasFutbolisticas.Security.Jwt.JwtProvider;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.RolService;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.EnvioMailService;
import backendAdministradorCompetenciasFutbolisticas.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    EnvioMailService envioMailService;

    @Autowired
    LogService logService;


    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new InvalidDataException(bindingResult);
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

            JwtDto jwtDto = new JwtDto(jwt);
            logService.guardarLogLoginUsuario(usuario);
            return new ResponseEntity(jwtDto, HttpStatus.OK);
        }catch (InternalAuthenticationServiceException e) {
            throw new UnauthorizedException("Nombre de usuario incorrecto");
        }catch (BadCredentialsException e){
            Usuario usuario = usuarioService.getByNombreUsuarioOrEmail(loginUsuario.getNombreUsuario()).get();
            Integer cantidadIntentosMax = 3;
            logService.guardarLogErrorLogin(usuario);
            if(!usuario.getNombreUsuario().equals("admin") && logService.cantidadLogUsuarioMayorOIgualAN(usuario, cantidadIntentosMax) && logService.ultimosNLogSonErrorLogin(usuario, cantidadIntentosMax) ){
                usuarioService.cambiarEstado(usuario.getId());
                logService.guardarLogBajaUsuarioLuegoDeNErrorLoginSeguidos(usuario);
                throw new BadRequestException("El usuario fue dado de baja por realizar " + cantidadIntentosMax + " intentos fallidos");
            }
            throw new UnauthorizedException("Contraseña incorrecta");
        }catch (LockedException e){
            Usuario usuario = usuarioService.getByNombreUsuarioOrEmail(loginUsuario.getNombreUsuario()).get();
            logService.guardarLogErrorLoginDeUsuarioInactivo(usuario);
            throw new UnauthorizedException("El usuario se encuentra bloqueado. Solicite su alta con el administrador del sistema");
        }
    }
}
