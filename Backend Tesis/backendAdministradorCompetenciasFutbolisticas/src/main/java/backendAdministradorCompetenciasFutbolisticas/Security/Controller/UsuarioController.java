package backendAdministradorCompetenciasFutbolisticas.Security.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.CambiarPasswordDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.NuevoUsuarioDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Rol;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Enums.RolNombre;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.RolService;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RolService rolService;


    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listado")
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> listaDeUsuarios = usuarioService.list();
        return new ResponseEntity(listaDeUsuarios, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detalle/{id}")
    public  ResponseEntity<Usuario> getDetalleUsuario(@PathVariable("id") Long id){
        if(!usuarioService.existById(id)){
            return new ResponseEntity(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioService.getById(id).get();
        return new ResponseEntity(usuario, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable ("id") long id){
        if (!usuarioService.existById(id)){
            return new ResponseEntity(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        try {
            usuarioService.delete(id);
            return new ResponseEntity(new Mensaje("Usuario borrado correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(new Mensaje("Fallo la operacion, usuario no borrado"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //@PreAuthorize(("hasRole('ADMIN')"))
    @PutMapping("/alta/{id}")
    public ResponseEntity<?> activarUsuario(@PathVariable ("id") Long id){
        Usuario usuario = usuarioService.getById(id).get();
        if (usuario == null){
            return new ResponseEntity(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        if (usuario.isActivo()) {
            return new ResponseEntity(new Mensaje("El usuario ya se encuentra Activo"), HttpStatus.BAD_REQUEST);
        }
        usuarioService.cambiarEstado(id);
        return  new ResponseEntity("Usuario dado de alta correctamente",HttpStatus.OK);
    }

    @PutMapping("/baja/{id}")
        public ResponseEntity<?> bajaUsuario(@PathVariable ("id") Long id){
        Usuario usuario = usuarioService.getById(id).get();
        if (usuario == null){
            return new ResponseEntity(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        if (!usuario.isActivo()){

            return new ResponseEntity(new Mensaje("El usuario ya se encuentra Inactivo"), HttpStatus.BAD_REQUEST);
        }
        usuarioService.cambiarEstado(id);
        return new ResponseEntity(new Mensaje("Usuario dado de baja correctamente"), HttpStatus.OK);

    }

    //@PreAuthorize("authenticated")
    @PutMapping("/cambiarContraseña/{id}")
    public ResponseEntity<?> cambiarContraseña(@PathVariable ("id") Long id, @Valid @RequestBody CambiarPasswordDto cambiarPasswordDto, BindingResult bindingResult){
        //UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioService.getById(id).get();
        if(bindingResult.hasErrors()){
            return  new ResponseEntity("Campos mal ingresados", HttpStatus.BAD_REQUEST);
        }
        if(!passwordEncoder.matches(cambiarPasswordDto.getPasswordActual(),usuario.getPassword())) {
            return  new ResponseEntity("La contraseña actual y la ingresada son incorrectas", HttpStatus.NOT_FOUND);
        }
        if(!cambiarPasswordDto.getPasswordNuevo().equals(cambiarPasswordDto.getRepetirPassword())){
            return new ResponseEntity("La contraseña nueva debe concidir", HttpStatus.NOT_FOUND);
        }
        if (passwordEncoder.matches(cambiarPasswordDto.getPasswordNuevo(),usuario.getPassword())){
            return new ResponseEntity("La contraseña nueva no puede ser igual a la actual",HttpStatus.NOT_FOUND);
        }
       // Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
        try{
            usuario.setPassword(passwordEncoder.encode(cambiarPasswordDto.getPasswordNuevo()));
            usuarioService.save(usuario);
            return  new ResponseEntity("Contraseña cambiada correctamente", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(new Mensaje("Fallo la operacion. La contraseña no fue cambiada"), HttpStatus.BAD_REQUEST);
        }

    }

    //TODO: Terminar esto
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
