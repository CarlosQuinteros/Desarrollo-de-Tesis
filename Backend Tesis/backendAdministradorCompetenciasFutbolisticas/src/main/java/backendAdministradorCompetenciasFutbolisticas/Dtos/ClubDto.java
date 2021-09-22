package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;

public class ClubDto {
    @NotBlank
    private String nombre;
    private String email;
    private LocalDate fechaFundacion;
    private Long idLocalidad;
    private ResponsableDto responsable;
    private Long asociacionDeportiva;

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

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public Long getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(Long idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public ResponsableDto getResponsable() {
        return responsable;
    }

    public void setResponsable(ResponsableDto responsable) {
        this.responsable = responsable;
    }

    public Long getAsociacionDeportiva() {
        return asociacionDeportiva;
    }

    public void setAsociacionDeportiva(Long asociacionDeportiva) {
        this.asociacionDeportiva = asociacionDeportiva;
    }
}
