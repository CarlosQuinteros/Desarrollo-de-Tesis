package backendAdministradorCompetenciasFutbolisticas.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer numero;

    private String descripcion;

    @ManyToOne
    private Competencia competencia;

    public Jornada(){}

    public Jornada(@NotNull Integer numero, String descripcion, @NotNull Competencia competencia) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.competencia = competencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }
}
