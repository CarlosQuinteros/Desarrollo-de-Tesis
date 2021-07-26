package backendAdministradorCompetenciasFutbolisticas.Security.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// no se creará en la base de datos implementará los privilegios de cada usuario
// implementa la clase propia de spring "UserDetails"
public class UsuarioPrincipal implements UserDetails {
    private String nombre;
    private String apellido;
    private String email;
    private String nombreUsuario;
    private String password;
    private boolean activo;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Collection<? extends GrantedAuthority> authorities;

    public UsuarioPrincipal(String nombre, String apellido, String email, String nombreUsuario, String password, boolean activo, Collection<? extends GrantedAuthority> authorities) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.authorities = authorities;
        this.activo = activo;
    }

    //asigna los privilegios a cada usuario
    public static UsuarioPrincipal build(Usuario usuario){
        List<GrantedAuthority>  authorities =
                usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol
                        .getRolNombre().name())).collect(Collectors.toList());
        return  new UsuarioPrincipal(usuario.getNombre(),usuario.getApellido(), usuario.getEmail(), usuario.getNombreUsuario(), usuario.getPassword(), usuario.isActivo() ,authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActivo();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActivo();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActivo();
    }

    @Override
    public boolean isEnabled() {
        return this.isActivo();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
