package backendAdministradorCompetenciasFutbolisticas.Entity;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Juez{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String documento;

    @NotNull
    @Column(unique = true)
    private String legajo;

    @NotNull
    private String nombres;

    @NotNull
    private String apellidos;

    public Juez(){

    }

    public Juez(@NotNull String nombres,@NotNull String apellidos, @NotNull String documento, @NotNull String legajo) {
        this.documento = documento;
        this.legajo = legajo;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

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
}
