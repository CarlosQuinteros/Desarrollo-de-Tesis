package backendAdministradorCompetenciasFutbolisticas.Security.Controller;

import backendAdministradorCompetenciasFutbolisticas.Security.Dto.JwtDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.LoginUsuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.NuevoUsuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Rol;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Enums.RolNombre;
import backendAdministradorCompetenciasFutbolisticas.Security.Jwt.JwtProvider;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.RolService;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class UsuarioController {

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

    //@PreAuthorize("hasRole('ADMIN)")
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity( "Campos al ingresados o email invalido", HttpStatus.BAD_REQUEST);
        if (usuarioService.existByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity("El nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
        if (usuarioService.existByEmail(nuevoUsuario.getEmail()))
            return  new ResponseEntity("El correo electronico ya existe", HttpStatus.BAD_REQUEST);
        Usuario usuario =
                new Usuario(nuevoUsuario.getNombre(),nuevoUsuario.getApellido(),nuevoUsuario.getEmail(), nuevoUsuario.getNombreUsuario(),
                        passwordEncoder.encode(nuevoUsuario.getPassword()));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getRolByNombre(RolNombre.ROLE_USER).get());
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ADMIN).get());
        if(nuevoUsuario.getRoles().contains("Encargado de jugadores"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ENCARGADO_DE_JUGADORES).get());
        if (nuevoUsuario.getRoles().contains("Encargado de sanciones"))
            roles.add(rolService.getRolByNombre(RolNombre.ROL_ENCARGADO_DE_SANCIONES).get());
        if (nuevoUsuario.getRoles().contains("Encargado de torneos"))
            roles.add(rolService.getRolByNombre(RolNombre.ROL_ENCARGADO_DE_TORNEOS).get());
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return new ResponseEntity("Usuario Guardado", HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity("Campos mal ingresados", HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);

    }
}
