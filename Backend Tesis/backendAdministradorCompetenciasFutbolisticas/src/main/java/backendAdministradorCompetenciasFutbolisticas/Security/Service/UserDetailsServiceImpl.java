package backendAdministradorCompetenciasFutbolisticas.Security.Service;

import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Entity.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String nombreUsuarioOrEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.getByNombreUsuarioOrEmail(nombreUsuarioOrEmail).get();
        return UsuarioPrincipal.build(usuario);
    }
}
