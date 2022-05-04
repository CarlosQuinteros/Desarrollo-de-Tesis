package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.NotBlank;

public class NuevoJuezDto {

    @NotBlank(message = "El documento no debe estar vacio")
    private String documento;

    @NotBlank(message = "El legajo no debe estar vacio")
    private String legajo;

    @NotBlank(message = "El nombre no debe estar vacio")
    private String nombres;

    @NotBlank(message = "El apellido no debe estar vacio")
    private String apellidos;

    public String getDocumento() {
        return documento;
    }

    public String getLegajo() {
        return legajo;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }
}
