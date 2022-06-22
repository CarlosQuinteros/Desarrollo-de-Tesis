package backendAdministradorCompetenciasFutbolisticas.Entity;

import backendAdministradorCompetenciasFutbolisticas.Enums.TipoCategoria;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String descripcion;

    @NotNull
    private Integer edadMaxima;

    @NotNull
    private Integer edadMinima;

    @NotNull
    private String tipo;

    public Categoria(){}

    public Categoria(@NotNull  String nombre, @NotNull String descripcion, @NotNull String tipo, @NotNull Integer edadMinima, @NotNull Integer edadMaxima){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(Integer edadMaxima) {
        this.edadMaxima = edadMaxima;
    }

    public Integer getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(Integer edadMinima) {
        this.edadMinima = edadMinima;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean jugadorPermitidoEnFecha(Jugador jugador, LocalDate fecha){
        Integer edad = jugador.getEdadEnFecha(fecha);
        return (edad >= this.edadMinima) && (edad <= this.edadMaxima);
    }

    public boolean esCategoriaHastaEdad(){
        return this.tipo.equals(TipoCategoria.HASTA_EDAD.name());
    }

    public boolean esCategoriaSinRestricciones() {
        return this.tipo.equals(TipoCategoria.SIN_RESTRICCIONES.name());
    }

    public boolean esCategoriaDesdeEdad(){
        return this.tipo.equals(TipoCategoria.DESDE_EDAD.name());
    }

    public boolean esCategoriaEntreEdades(){
        return this.tipo.equals(TipoCategoria.ENTRE_EDADES.name());
    }
}
