package backendAdministradorCompetenciasFutbolisticas.Dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class JuezRolDto {
    @NotNull(message = "El partido es obligatorio")
    private Long idPartido;

    @NotNull(message = "El juez no debe estar vacio")
    private Long idJuez;

    @NotBlank(message = "El rol del juez no debe estar vacio")
    private String rol;

    public Long getIdPartido() {
        return idPartido;
    }

    public Long getIdJuez() {
        return idJuez;
    }

    public String getRol() {
        return rol;
    }
}
