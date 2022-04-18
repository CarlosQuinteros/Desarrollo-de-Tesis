package backendAdministradorCompetenciasFutbolisticas.Security.Dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

public class ActualizarUsuarioDto {
    @NotBlank(message = "El nombre no debe estar vacio")
    private String nombre;
    @NotBlank(message = "El apellido no debe estar vacio")
    private String apellido;
    @Email(message = "El correo es obligatorio")
    private String email;
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;
    private Set<String> roles = new HashSet<>();

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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
