package backendAdministradorCompetenciasFutbolisticas.Security.Dto;

import javax.validation.constraints.NotBlank;

public class CambiarPasswordDto {
    @NotBlank(message = "La contraseña actual no debe estar vacia")
    private String passwordActual;
    @NotBlank(message = "La contraseña nueva no debe estar vacia")
    private String passwordNuevo;
    @NotBlank(message = "Confirmar la contraseña es obligatorio")
    private String repetirPassword;

    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    public String getPasswordNuevo() {
        return passwordNuevo;
    }

    public void setPasswordNuevo(String passwordNuevo) {
        this.passwordNuevo = passwordNuevo;
    }

    public String getRepetirPassword() {
        return repetirPassword;
    }

    public void setRepetirPassword(String repetirPassword) {
        this.repetirPassword = repetirPassword;
    }
}
