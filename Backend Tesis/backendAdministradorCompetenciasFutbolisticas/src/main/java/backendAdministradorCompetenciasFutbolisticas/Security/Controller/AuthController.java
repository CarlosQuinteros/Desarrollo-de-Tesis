package backendAdministradorCompetenciasFutbolisticas.Security.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody NuevoUsuarioDto usuarioDto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity( "Campos al ingresados o email invalido", HttpStatus.BAD_REQUEST);
        if (usuarioService.existByNombreUsuario(usuarioDto.getNombreUsuario()))
            return new ResponseEntity("El nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
        if (usuarioService.existByEmail(usuarioDto.getEmail()))
            return  new ResponseEntity("El correo electronico ya existe", HttpStatus.BAD_REQUEST);

        Usuario usuario =
                new Usuario(usuarioDto.getNombre(), usuarioDto.getApellido(), usuarioDto.getEmail(), usuarioDto.getNombreUsuario(),
                        passwordEncoder.encode(usuarioDto.getPassword()));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getRolByNombre(RolNombre.ROLE_USER).get());
        if(usuarioDto.getRoles().contains("Admin"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ADMIN).get());
        if(usuarioDto.getRoles().contains("Encargado de jugadores"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ENCARGADO_DE_JUGADORES).get());
        if (usuarioDto.getRoles().contains("Encargado de sanciones"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ENCARGADO_DE_SANCIONES).get());
        if (usuarioDto.getRoles().contains("Encargado de torneos"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ENCARGADO_DE_TORNEOS).get());
        usuario.setRoles(roles);
        try {
            usuarioService.save(usuario);
            envioMailService.sendEmailUsuarioCreado(usuarioDto);
            return new ResponseEntity(new Mensaje("Nuevo usuario guardado"), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion, usuario no guardado"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos mal ingresados"), HttpStatus.BAD_REQUEST);
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);
            //UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            //JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
            JwtDto jwtDto = new JwtDto(jwt);

            return new ResponseEntity(jwtDto, HttpStatus.OK);
        }catch (InternalAuthenticationServiceException e) {
            return new ResponseEntity(new Mensaje("Nombre de usuario o contraseña incorrectos"), HttpStatus.UNAUTHORIZED);
        }catch (BadCredentialsException e){
            return new ResponseEntity(new Mensaje("Contraseña incorrecta"),HttpStatus.UNAUTHORIZED);
        }catch (LockedException e){
            return new ResponseEntity(new Mensaje("El usuario se encuentra bloqueado"), HttpStatus.UNAUTHORIZED);
        }
    }
}
