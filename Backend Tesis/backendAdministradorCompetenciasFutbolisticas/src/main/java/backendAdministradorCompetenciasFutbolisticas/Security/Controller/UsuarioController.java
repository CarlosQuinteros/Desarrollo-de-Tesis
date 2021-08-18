package backendAdministradorCompetenciasFutbolisticas.Security.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.CambiarPasswordDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.NuevoUsuarioDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Rol;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Enums.RolNombre;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.RolService;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.EnvioMailService;
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
import java.util.Optional;
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

    @Autowired
    EnvioMailService envioMailService;


    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody NuevoUsuarioDto usuarioDto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity( new Mensaje("Campos al ingresados o email invalido"), HttpStatus.BAD_REQUEST);
        if (usuarioService.existByNombreUsuario(usuarioDto.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        if (usuarioService.existByEmail(usuarioDto.getEmail()))
            return  new ResponseEntity(new Mensaje("El correo electronico ya existe"), HttpStatus.BAD_REQUEST);

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
            roles.add(rolService.getRolByNombre(RolNombre.ROL_ENCARGADO_DE_SANCIONES).get());
        if (usuarioDto.getRoles().contains("Encargado de torneos"))
            roles.add(rolService.getRolByNombre(RolNombre.ROL_ENCARGADO_DE_TORNEOS).get());
        usuario.setRoles(roles);
        try {
            usuarioService.save(usuario);
            //envioMailService.sendEmailUsuarioCreado(usuarioDto); tarda varios segundos
            return new ResponseEntity(new Mensaje("Nuevo usuario guardado"), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("Fallo la operacion, usuario no guardado"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    @GetMapping("/detalle/nombreUsuario/{nombreUsuario}")
    public ResponseEntity<Usuario> getDetalleUsuarioPorNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario){
        Optional<Usuario> usuarioOptional = usuarioService.getByNombreUsuario(nombreUsuario);
        if (!usuarioOptional.isPresent()){
            return new ResponseEntity(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioService.getByNombreUsuario(nombreUsuario).get();
        return new ResponseEntity(usuario, HttpStatus.OK);

    }

    //@PreAuthorize("hasRole('ADMIN')")
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
        if (!usuarioService.existById(id)){
            return new ResponseEntity(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioService.getById(id).get();
        if (usuario.isActivo()) {
            return new ResponseEntity(new Mensaje("El usuario ya se encuentra Activo"), HttpStatus.BAD_REQUEST);
        }
        usuarioService.cambiarEstado(id);
        return  new ResponseEntity(new Mensaje("Usuario dado de alta correctamente"),HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/baja/{id}")
        public ResponseEntity<?> bajaUsuario(@PathVariable ("id") Long id){

        if (!usuarioService.existById(id)){
            return new ResponseEntity(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioService.getById(id).get();
        if (!usuario.isActivo()){

            return new ResponseEntity(new Mensaje("El usuario ya se encuentra Inactivo"), HttpStatus.BAD_REQUEST);
        }
        usuarioService.cambiarEstado(id);
        return new ResponseEntity(new Mensaje("Usuario dado de baja correctamente"), HttpStatus.OK);

    }

    //@PreAuthorize("authenticated")
    @PutMapping("/cambiarContrasenia/{id}")
    public ResponseEntity<?> cambiarContrasenia(@PathVariable ("id") Long id, @Valid @RequestBody CambiarPasswordDto cambiarPasswordDto, BindingResult bindingResult){
        //UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!usuarioService.existById(id)){
            return new ResponseEntity(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioService.getById(id).get();
        if(bindingResult.hasErrors()){
            return  new ResponseEntity(new Mensaje("Campos mal ingresados"), HttpStatus.BAD_REQUEST);
        }
        if(!passwordEncoder.matches(cambiarPasswordDto.getPasswordActual(),usuario.getPassword())) {
            return  new ResponseEntity(new Mensaje("La contraseña actual y la ingresada son incorrectas"), HttpStatus.NOT_FOUND);
        }
        if(!cambiarPasswordDto.getPasswordNuevo().equals(cambiarPasswordDto.getRepetirPassword())){
            return new ResponseEntity(new Mensaje("La contraseña nueva debe concidir"), HttpStatus.NOT_FOUND);
        }
        if (passwordEncoder.matches(cambiarPasswordDto.getPasswordNuevo(),usuario.getPassword())){
            return new ResponseEntity(new Mensaje("La contraseña nueva no puede ser igual a la actual"),HttpStatus.NOT_FOUND);
        }
        try{
            usuario.setPassword(passwordEncoder.encode(cambiarPasswordDto.getPasswordNuevo()));
            usuarioService.save(usuario);
            return  new ResponseEntity(new Mensaje("Contraseña cambiada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(new Mensaje("Fallo la operacion. La contraseña no fue cambiada"), HttpStatus.BAD_REQUEST);
        }

    }


    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editar/{id}")
    public  ResponseEntity<Usuario> actualizarUsuario(@PathVariable ("id") Long id, @RequestBody NuevoUsuarioDto usuarioDto ){
        Optional<Usuario> usuarioOptional = usuarioService.getById(id);
        if(!usuarioOptional.isPresent()) {
            return new ResponseEntity(new Mensaje("No existe el usuario"), HttpStatus.NOT_FOUND);
        }
        Usuario usuarioActualizar = usuarioOptional.get();
        if (usuarioService.existByNombreUsuario(usuarioDto.getNombreUsuario()) && !usuarioActualizar.getNombreUsuario().equals(usuarioDto.getNombreUsuario())){
            return new ResponseEntity(new Mensaje("El nombre de usuario ya existe"),HttpStatus.BAD_REQUEST);
        }
        if(usuarioService.existByEmail(usuarioDto.getEmail()) && !usuarioActualizar.getEmail().equals(usuarioDto.getEmail())){
            return new ResponseEntity(new Mensaje("El correo electrónico ya existe"), HttpStatus.BAD_REQUEST);
        }
        usuarioActualizar.setNombre(usuarioDto.getNombre());
        usuarioActualizar.setApellido(usuarioDto.getApellido());
        usuarioActualizar.setNombreUsuario(usuarioDto.getNombreUsuario());
        usuarioActualizar.setEmail(usuarioDto.getEmail());

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
        usuarioActualizar.setRoles(roles);
        try{
            usuarioService.save(usuarioActualizar);
            return new ResponseEntity(new Mensaje("Usuario actualizado"), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity(new Mensaje("Fallo la operacion, usuario no actualizado"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> cantidadTotalDeUsuarios(){
        int cantidad = usuarioService.cantidadUsuarios();
        return new ResponseEntity(cantidad, HttpStatus.OK);
    }

    @GetMapping("/total-activos")
    public ResponseEntity<?> cantidadTotalDeActivos(){
        int cantidad = usuarioService.cantidadDeActivos();
        return new ResponseEntity(cantidad, HttpStatus.OK);

    }

    @GetMapping("/total-inactivos")
    public ResponseEntity<?> cantidadTotalDeInactivos(){
        int cantidad = usuarioService.cantidadDeInactivos();
        return new ResponseEntity(cantidad, HttpStatus.OK);

    }
}
