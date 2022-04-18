package backendAdministradorCompetenciasFutbolisticas.Security.Dto;

import javax.validation.constraints.NotBlank;

public class LoginUsuario {
    @NotBlank(message = "El nombre de usuario no debe estar vacio")
    private String nombreUsuario;
    @NotBlank(message = "La contraseña no debe estar vacia")
    private String password;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
