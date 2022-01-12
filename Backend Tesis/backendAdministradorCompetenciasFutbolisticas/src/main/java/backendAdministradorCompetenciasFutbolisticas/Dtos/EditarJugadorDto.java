package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class EditarJugadorDto {
    @NotBlank(message = "El campo nombres es obligatorio")
    private String nombres;

    @NotBlank(message = "El campo apellidos es obligatorio")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria ")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El documento es obligatorio")
    private  String documento;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

}
