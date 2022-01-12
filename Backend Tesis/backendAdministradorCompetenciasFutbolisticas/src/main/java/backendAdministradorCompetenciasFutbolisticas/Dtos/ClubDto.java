package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.NotBlank;

public class ClubDto {

    @NotBlank(message = "El alias no debe ser nulo")
    private  String alias;

    @NotBlank(message = "El nombre no debe ser nulo")
    private String nombre;

    @NotBlank(message = "El email no debe ser nulo")
    private String email;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
