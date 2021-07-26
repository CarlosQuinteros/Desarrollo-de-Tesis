package backendAdministradorCompetenciasFutbolisticas.Security.Controller;

import backendAdministradorCompetenciasFutbolisticas.Security.Dto.CambiarPasswordDto;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
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
        if(usuarioDto.getRoles().contains("admin"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ADMIN).get());
        if(usuarioDto.getRoles().contains("Encargado de jugadores"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ENCARGADO_DE_JUGADORES).get());
        if (usuarioDto.getRoles().contains("Encargado de sanciones"))
            roles.add(rolService.getRolByNombre(RolNombre.ROL_ENCARGADO_DE_SANCIONES).get());
        if (usuarioDto.getRoles().contains("Encargado de torneos"))
            roles.add(rolService.getRolByNombre(RolNombre.ROL_ENCARGADO_DE_TORNEOS).get());
        usuario.setRoles(roles);
        try {
            usuarioService.save(usuario);
            envioMailService.sendEmailUsuarioCreado(usuarioDto);
            return new ResponseEntity("Usuario Guardado", HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>("Fallo la operacion, usuario no guardado", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity("Campos mal ingresados", HttpStatus.BAD_REQUEST);
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
            return new ResponseEntity(jwtDto, HttpStatus.OK);
        }catch (InternalAuthenticationServiceException e){

            return  new ResponseEntity("Nombre de usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
        }catch (LockedException e){
            return new ResponseEntity("El usuario se encuentra bloqueado", HttpStatus.UNAUTHORIZED);
        }


    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listaUsuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> listaDeUsuarios = usuarioService.list();
        return new ResponseEntity(listaDeUsuarios, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detalle/{id}")
    public  ResponseEntity<Usuario> getDetalleUsuario(@PathVariable("id") Long id){
        if(!usuarioService.existById(id)){
            return new ResponseEntity("No existe el usuario", HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioService.getById(id).get();
        return new ResponseEntity(usuario, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable ("id") long id){
        if (!usuarioService.existById(id)){
            return new ResponseEntity("No existe el usuario", HttpStatus.NOT_FOUND);
        }
        try {
            usuarioService.delete(id);
            return new ResponseEntity("Usuario borrado correctamente", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity("Fallo la operacion, usuario no borrado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize(("hasRole('ADMIN')"))
    @PutMapping("/cambiarEstado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable ("id") Long id){
        if (!usuarioService.existById(id)){
            return new ResponseEntity("No existe el usuario", HttpStatus.NOT_FOUND);
        }
        usuarioService.cambiarEstado(id);
        return  new ResponseEntity("Usuario actualizado correctamente",HttpStatus.OK);
    }

    @PreAuthorize("authenticated")
    @PutMapping("/cambiarContraseña")
    public ResponseEntity<?> cambiarContraseña(Authentication authentication, @Valid @RequestBody CambiarPasswordDto cambiarPasswordDto, BindingResult bindingResult){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if(bindingResult.hasErrors()){
            return  new ResponseEntity("Campos mal ingresados", HttpStatus.BAD_REQUEST);
        }
        if(!passwordEncoder.matches(cambiarPasswordDto.getPasswordActual(),userDetails.getPassword())) {
            return  new ResponseEntity("La contraseña actual y la ingresada son incorrectas", HttpStatus.NOT_FOUND);
        }
        if(!cambiarPasswordDto.getPasswordNuevo().equals(cambiarPasswordDto.getRepetirPassword())){
            return new ResponseEntity("La contraseña nueva debe concidir", HttpStatus.NOT_FOUND);
        }
        if (passwordEncoder.matches(cambiarPasswordDto.getPasswordNuevo(),userDetails.getPassword())){
            return new ResponseEntity("La contraseña nueva no puede ser igual a la actual",HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
        usuario.setPassword(passwordEncoder.encode(cambiarPasswordDto.getPasswordNuevo()));
        usuarioService.save(usuario);
        return  new ResponseEntity("Contraseña cambiada correctamente", HttpStatus.OK);
    }

    @PutMapping("/actualizar/{id}")
    public  ResponseEntity<Usuario> actualizarUsuario(@PathVariable ("id") Long id, @RequestBody NuevoUsuarioDto usuarioDto ){
        if(!usuarioService.existById(id)) {
            return new ResponseEntity("No existe el usuario", HttpStatus.NOT_FOUND);
        }
        if (usuarioService.existByNombreUsuario(usuarioDto.getNombreUsuario())){
            return new ResponseEntity("El nombre de usuario ya existe",HttpStatus.BAD_REQUEST);
        }
        if(usuarioService.existByEmail(usuarioDto.getEmail())){
            return new ResponseEntity("El correo electrónico ya existe", HttpStatus.BAD_REQUEST);
        }
        Usuario usuario =
                new Usuario(usuarioDto.getNombre(), usuarioDto.getApellido(), usuarioDto.getEmail(), usuarioDto.getNombreUsuario(),
                        passwordEncoder.encode(usuarioDto.getPassword()));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getRolByNombre(RolNombre.ROLE_USER).get());
        if(usuarioDto.getRoles().contains("admin"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ADMIN).get());
        if(usuarioDto.getRoles().contains("Encargado de jugadores"))
            roles.add(rolService.getRolByNombre(RolNombre.ROLE_ENCARGADO_DE_JUGADORES).get());
        if (usuarioDto.getRoles().contains("Encargado de sanciones"))
            roles.add(rolService.getRolByNombre(RolNombre.ROL_ENCARGADO_DE_SANCIONES).get());
        if (usuarioDto.getRoles().contains("Encargado de torneos"))
            roles.add(rolService.getRolByNombre(RolNombre.ROL_ENCARGADO_DE_TORNEOS).get());
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return null;

    }
}
