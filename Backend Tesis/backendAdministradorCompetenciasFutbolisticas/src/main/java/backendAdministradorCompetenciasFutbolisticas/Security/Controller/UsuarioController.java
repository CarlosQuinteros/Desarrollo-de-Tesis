package backendAdministradorCompetenciasFutbolisticas.Security.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Dtos.NuevoJugadorDto;
import backendAdministradorCompetenciasFutbolisticas.Entity.Log;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InternalServerErrorException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.ActualizarUsuarioDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.CambiarPasswordDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.NuevoUsuarioDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.PerfilUsuarioDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Rol;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Enums.RolNombre;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.RolService;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import backendAdministradorCompetenciasFutbolisticas.Service.EnvioMailService;
import backendAdministradorCompetenciasFutbolisticas.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    LogService logService;


    /*
        Metodo que permite crear un nuevo usuario
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody NuevoUsuarioDto usuarioDto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            throw new InvalidDataException(bindingResult);
        if (usuarioService.existByNombreUsuario(usuarioDto.getNombreUsuario()))
            throw new BadRequestException("El nombre de usuario " + usuarioDto.getNombreUsuario() + " ya existe");
        if (usuarioService.existByEmail(usuarioDto.getEmail()))
            throw new BadRequestException("El correo " + usuarioDto.getEmail() + " ya existe");
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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioLogueado = usuarioService.getUsuarioLogueado(auth);
            usuarioService.save(usuario);
            logService.guardarLogCreacionUsuario(usuario, usuarioLogueado);
            //envioMailService.sendEmailUsuarioCreado(usuarioDto); tarda varios segundos
            return new ResponseEntity(new Mensaje("Nuevo usuario guardado"), HttpStatus.CREATED);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion, usuario no guardado");
        }
    }

    @PreAuthorize(("hasRole('ADMIN')"))
    @PostMapping("/envio-email/usuario-nuevo")
    public ResponseEntity<?> enviarEmailUsuarioNuevo(@Valid @RequestBody NuevoUsuarioDto nuevoUsuarioDto){
        try{
            envioMailService.sendEmailUsuarioCreado(nuevoUsuarioDto);
            return new ResponseEntity(new Mensaje("Email enviado correctamente"), HttpStatus.OK);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. Email no enviado");
        }
    }


    /*
        Metodo que permite listar todos los usuarios
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listado")
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> listaDeUsuarios = usuarioService.list();
        return new ResponseEntity(listaDeUsuarios, HttpStatus.OK);
    }

    /*
        Metodo que retorna un usuario por id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detalle/{id}")
    public  ResponseEntity<Usuario> getDetalleUsuario(@PathVariable("id") Long id){
        if(!usuarioService.existById(id)){
            throw new BadRequestException("El usuario con ID: " + id + " no existe");
        }
        Usuario usuario = usuarioService.getById(id).get();
        return new ResponseEntity(usuario, HttpStatus.OK);
    }

    /*
        Metodo que retorna un usuario por su nombre de usuario o login
     */
    @GetMapping("/detalle/nombreUsuario/{nombreUsuario}")
    public ResponseEntity<Usuario> getDetalleUsuarioPorNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario){
        Optional<Usuario> usuarioOptional = usuarioService.getByNombreUsuario(nombreUsuario);
        if (!usuarioOptional.isPresent()){
            throw new BadRequestException("El usuario: " + nombreUsuario + " no existe");
        }
        Usuario usuario = usuarioService.getByNombreUsuario(nombreUsuario).get();
        return new ResponseEntity(usuario, HttpStatus.OK);

    }


    /*
        Metodo que permite eliminar a un usuario y su actividad de logs. solo en desarrollo
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable ("id") long id){
        if (!usuarioService.existById(id)){
            throw new BadRequestException("El usuario con ID: " + id + " no existe");
        }
        Usuario usuario = usuarioService.getById(id).get();
        if (usuario.getNombreUsuario().contains("admin")){
            throw new BadRequestException("El usuario administrador no se puede eliminar");
        }
        try {
            usuarioService.delete(id);
            return new ResponseEntity(new Mensaje("Usuario borrado correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            throw new InternalServerErrorException("Fallo la operacion. El usuario no se borro correctamente");
        }
    }

    /*
        Metodo que permite actividad de usuario. solo en desarrollo
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/actividad")
    public ResponseEntity<?> deleteActividadUser(@PathVariable ("id") long id){
        if (!usuarioService.existById(id)){
            throw new BadRequestException("El usuario con ID: " + id + " no existe");
        }
        Usuario usuario = usuarioService.getById(id).get();
        logService.eliminarActividadDeUsuario(usuario.getId());
        return new ResponseEntity<>(new Mensaje("Actividad de usuario eliminada correctamente"),HttpStatus.OK);
    }


    /*
        Metodo que permite dar de alta a un usurio
     */
    @PreAuthorize(("hasRole('ADMIN')"))
    @PutMapping("/alta/{id}")
    public ResponseEntity<?> activarUsuario(@PathVariable ("id") Long id){
        if (!usuarioService.existById(id)){
            throw new BadRequestException("El usuario con ID: " + id + " no existe");
        }
        Usuario usuario = usuarioService.getById(id).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.getUsuarioLogueado(auth);
        if (usuario.isActivo()) {
            throw new BadRequestException("El usuario ya se encuentra Activo");
        }
        usuarioService.cambiarEstado(id);
        logService.guardarAltaUsuario(usuario, usuarioLogueado);
        return  new ResponseEntity(new Mensaje("Usuario dado de alta correctamente"),HttpStatus.OK);
    }


    /*
        Metodo que permite  dar de baja a un usuario
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/baja/{id}")
        public ResponseEntity<?> bajaUsuario(@PathVariable ("id") Long id){

        if (!usuarioService.existById(id)){
            throw new BadRequestException("El usuario con ID: " + id + " no existe");
        }
        Usuario usuario = usuarioService.getById(id).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.getUsuarioLogueado(auth);

        if (!usuario.isActivo()){
            throw new BadRequestException("El usuario ya se encuentra Inactivo");
        }
        if(usuario.getNombreUsuario().contains("admin")){
            throw new BadRequestException("El usuario administrador no puede darse de baja");
        }
        usuarioService.cambiarEstado(id);
        logService.guardarBajaUsuario(usuario, usuarioLogueado);
        return new ResponseEntity(new Mensaje("Usuario dado de baja correctamente"), HttpStatus.OK);

    }


    /*
        Metodo que permite a un usuario cambiar su contraseña
    */
    @PreAuthorize("authenticated")
    @PutMapping("/cambiarContrasenia/{id}")
    public ResponseEntity<?> cambiarContrasenia(@PathVariable ("id") Long id, @Valid @RequestBody CambiarPasswordDto cambiarPasswordDto, BindingResult bindingResult){

        if (!usuarioService.existById(id)){
            throw new BadRequestException("El usuario con ID: " + id + " no existe");
        }
        Usuario usuario = usuarioService.getById(id).get();
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        if(!passwordEncoder.matches(cambiarPasswordDto.getPasswordActual(),usuario.getPassword())) {
            throw new BadRequestException("La contraseña actual y la ingresada son incorrectas");
        }
        if(!cambiarPasswordDto.getPasswordNuevo().equals(cambiarPasswordDto.getRepetirPassword())){
            throw new BadRequestException("La contraseña nueva debe concidir con la ingresada");
        }
        if (passwordEncoder.matches(cambiarPasswordDto.getPasswordNuevo(),usuario.getPassword())){
            throw new BadRequestException("La contraseña nueva no puede ser igual a la actual");
        }
        try{
            usuario.setPassword(passwordEncoder.encode(cambiarPasswordDto.getPasswordNuevo()));
            usuarioService.save(usuario);
            return  new ResponseEntity(new Mensaje("Contraseña cambiada correctamente"), HttpStatus.OK);
        }catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion. La contraseña no fue cambiada");
        }

    }


    /*
        Metodo que permite editar un usuario, sus datos personales y sus roles
    */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editar/{id}")
    public  ResponseEntity<Usuario> actualizarUsuario(@PathVariable ("id") Long id, @Valid @RequestBody ActualizarUsuarioDto usuarioDto, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Optional<Usuario> usuarioOptional = usuarioService.getById(id);
        if(!usuarioOptional.isPresent()) {
            throw new ResourceNotFoundException("El usuario con ID: " + id + " no existe");
        }
        Usuario usuarioActualizar = usuarioOptional.get();
        if(usuarioActualizar.getNombreUsuario().contains("admin")){
            throw new BadRequestException("El usuario administrador no puede editarse");
        }
        if (usuarioService.existByNombreUsuario(usuarioDto.getNombreUsuario()) && !usuarioActualizar.getNombreUsuario().equals(usuarioDto.getNombreUsuario())){
            throw new BadRequestException("El nombre de usuario " + usuarioDto.getNombreUsuario() + " ya existe");
        }
        if(usuarioService.existByEmail(usuarioDto.getEmail()) && !usuarioActualizar.getEmail().equals(usuarioDto.getEmail())){
            throw new BadRequestException("El correo " + usuarioDto.getEmail() + " ya existe");
        }
        usuarioActualizar.setNombre(usuarioDto.getNombre());
        usuarioActualizar.setApellido(usuarioDto.getApellido());
        usuarioActualizar.setNombreUsuario(usuarioDto.getNombreUsuario());
        usuarioActualizar.setEmail(usuarioDto.getEmail());

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
        usuarioActualizar.setRoles(roles);
        try{
            usuarioService.save(usuarioActualizar);
            return new ResponseEntity(usuarioActualizar, HttpStatus.OK);

        } catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion, usuario no actualizado");
        }
    }

    /*
        Metodo que permite a un usuario actualizar sus datos personales, no sus roles.
    */
    @PutMapping("/perfil/actualizarDatos/{id}")
    public ResponseEntity<Usuario> actualizarDatosPerfil(@PathVariable ("id") Long id, @Valid @RequestBody  PerfilUsuarioDto usuarioDto, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Optional<Usuario> usuarioOptional = usuarioService.getById(id);
        if (!usuarioOptional.isPresent()){
            throw new ResourceNotFoundException("El usuario con ID: " + id + " no existe");
        }
        Usuario usuarioActualizar = usuarioOptional.get();
        if(usuarioActualizar.getNombreUsuario().contains("admin")){
            throw new BadRequestException("El usuario administrador no puede editarse");
        }
        if (usuarioService.existByNombreUsuario(usuarioDto.getNombreUsuario()) && !usuarioActualizar.getNombreUsuario().equals(usuarioDto.getNombreUsuario())){
            throw new BadRequestException("El nombre de usuario " + usuarioDto.getNombreUsuario() + " ya existe");
        }
        if(usuarioService.existByEmail(usuarioDto.getEmail()) && !usuarioActualizar.getEmail().equals(usuarioDto.getEmail())){
            throw new BadRequestException("El correo " + usuarioDto.getEmail() + " ya existe");
        }
        usuarioActualizar.setNombre(usuarioDto.getNombre());
        usuarioActualizar.setApellido(usuarioDto.getApellido());
        usuarioActualizar.setNombreUsuario(usuarioDto.getNombreUsuario());
        usuarioActualizar.setEmail(usuarioDto.getEmail());
        try{
            usuarioService.save(usuarioActualizar);
            return new ResponseEntity(usuarioActualizar, HttpStatus.OK);

        } catch (Exception e){
            throw new InternalServerErrorException("Fallo la operacion, usuario no actualizado");
        }

    }

    /*
        Metodo que retorna la cantidad de usuarios
    */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total")
    public ResponseEntity<?> cantidadTotalDeUsuarios(){
        int cantidad = usuarioService.cantidadUsuarios();
        return new ResponseEntity(cantidad, HttpStatus.OK);
    }


    /*
        Metodo que retorna la cantidad de usuarios activos
    */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total-activos")
    public ResponseEntity<?> cantidadTotalDeActivos(){
        int cantidad = usuarioService.cantidadDeActivos();
        return new ResponseEntity(cantidad, HttpStatus.OK);

    }


    /*
        Metodo que retorna la cantidad de usuarios dados de baja
    */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total-inactivos")
    public ResponseEntity<?> cantidadTotalDeInactivos(){
        int cantidad = usuarioService.cantidadDeInactivos();
        return new ResponseEntity(cantidad, HttpStatus.OK);

    }

    /*
        Metodo que retorna el historial de logs del usuario
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/actividad/{id}")
    public ResponseEntity<List<Log>> listadoLogsUsuario(@PathVariable ("id") Long id){
        List<Log> actividad = logService.logsPorUsuario(id);
        return new ResponseEntity<>(actividad, HttpStatus.OK);
    }
}
