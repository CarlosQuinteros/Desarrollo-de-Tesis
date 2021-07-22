package backendAdministradorCompetenciasFutbolisticas.Security.Dto;

import javax.validation.constraints.NotBlank;

public class CambiarPasswordDto {
    @NotBlank
    private String passwordActual;
    @NotBlank
    private String passwordNuevo;
    @NotBlank
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
