package backendAdministradorCompetenciasFutbolisticas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    String nombre;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "provincia", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("provincia")
    List<Localidad> localidades = new ArrayList<Localidad>();

    public Provincia(){}

    public Provincia(@NotNull String nombre) {
        this.nombre = nombre;
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

    public List<Localidad> getLocalidades() {
        Collections.sort(this.localidades, (x,y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
        return localidades;
    }

    public void setLocalidades(List<Localidad> localidades) {
        this.localidades = localidades;
    }

    public boolean eliminarLocalidad(Localidad localidad){
        return getLocalidades().remove(localidad);
    }
}
