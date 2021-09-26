package backendAdministradorCompetenciasFutbolisticas.CrearRoles;

import backendAdministradorCompetenciasFutbolisticas.Entity.EstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Enums.NombreEstadoJugador;
import backendAdministradorCompetenciasFutbolisticas.Repository.EstadoJugadorRepository;
import backendAdministradorCompetenciasFutbolisticas.Security.Dto.NuevoUsuarioDto;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Rol;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Enums.RolNombre;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.RolService;
import backendAdministradorCompetenciasFutbolisticas.Security.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

//se ejecuta una sola vez para crear los roles, usuario administrador y estado de jugadores
/*
@Component
public class CrearRoles implements CommandLineRunner {

    @Autowired
    RolService rolService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EstadoJugadorRepository estadoJugadorRepository;

    @Override
    public void run(String... args) throws Exception {

        Rol admin = new Rol(RolNombre.ROLE_ADMIN);
        Rol user =  new Rol(RolNombre.ROLE_USER);
        Rol encargadoJugadoresYClubes = new Rol(RolNombre.ROLE_ENCARGADO_DE_JUGADORES);
        Rol encargadoSanciones = new Rol(RolNombre.ROLE_ENCARGADO_DE_SANCIONES);
        Rol encargadoTorneos  = new Rol(RolNombre.ROLE_ENCARGADO_DE_TORNEOS);


        rolService.save(admin);
        rolService.save(encargadoSanciones);
        rolService.save(encargadoJugadoresYClubes);
        rolService.save(encargadoTorneos);
        rolService.save(user);


        Usuario usuarioAdmin =  new Usuario();
        usuarioAdmin.setNombre("Administrador");
        usuarioAdmin.setApellido("Administrador");
        usuarioAdmin.setEmail("admcompetenciasfutbolisticas@gmail.com");
        usuarioAdmin.setNombreUsuario("admin");
        usuarioAdmin.setPassword("admin1234*");
        Set<Rol> roles = new HashSet<>();
        roles.add(admin);
        usuarioAdmin.setRoles(roles);

        usuarioService.save(usuarioAdmin);

        EstadoJugador estadoActivo = new EstadoJugador(NombreEstadoJugador.ACTIVO);
        EstadoJugador estadoInactivo = new EstadoJugador(NombreEstadoJugador.INACTIVO);
        EstadoJugador estadoSuspendido = new EstadoJugador(NombreEstadoJugador.SUSPENDIDO);
        EstadoJugador estadoRetirado = new EstadoJugador(NombreEstadoJugador.RETIRADO);

        estadoJugadorRepository.save(estadoActivo);
        estadoJugadorRepository.save(estadoInactivo);
        estadoJugadorRepository.save(estadoSuspendido);
        estadoJugadorRepository.save(estadoRetirado);
    }
}
*/