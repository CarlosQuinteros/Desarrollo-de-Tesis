package backendAdministradorCompetenciasFutbolisticas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Localidad{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties("localidades")
    private Provincia provincia;

    public Localidad(){}

    public Localidad(@NotNull String nombre, @NotNull Provincia provincia) {
        this.nombre = nombre;
        this.provincia = provincia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

}
