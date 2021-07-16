package backendAdministradorCompetenciasFutbolisticas.Security.Service;

import backendAdministradorCompetenciasFutbolisticas.Security.Entity.Usuario;
import backendAdministradorCompetenciasFutbolisticas.Security.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public boolean existByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    public boolean existByEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    public List<Usuario> list(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getById(Long id){
        return  usuarioRepository.findById(id);
    }
}
