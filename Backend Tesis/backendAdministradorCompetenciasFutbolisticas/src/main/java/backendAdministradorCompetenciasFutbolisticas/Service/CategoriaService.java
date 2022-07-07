package backendAdministradorCompetenciasFutbolisticas.Service;

import backendAdministradorCompetenciasFutbolisticas.Entity.Categoria;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.ResourceNotFoundException;
import backendAdministradorCompetenciasFutbolisticas.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CompetenciaService competenciaService;

    public Categoria guardarCategoria(Categoria categoria){
        //dependiendo tipo categoria llamo a un metodo
        if(categoria.esCategoriaSinRestricciones()) return crearCategoriaSinRestricciones(categoria);
        if(categoria.esCategoriaHastaEdad()) return crearCategoriaHastaEdad(categoria);
        if(categoria.esCategoriaDesdeEdad()) return crearCategoriaDesdeEdad(categoria);
        if(categoria.esCategoriaEntreEdades()) return crearCategoriaEntreEdades(categoria);
        throw new BadRequestException("Tipo de categoria incorrecto: "+ categoria.getTipo());
    }

    public Categoria crearCategoriaSinRestricciones(Categoria categoria){
        //Edades 5 y 65
        categoria.setEdadMinima(5);
        categoria.setEdadMaxima(65);
        return categoriaRepository.save(categoria);
    }

    public Categoria crearCategoriaHastaEdad(Categoria categoria){
        //edades 5 y edad
        if(categoria.getEdadMaxima().equals(categoria.getEdadMinima())){
            throw new BadRequestException("Para crear categoria tipo: HASTA EDAD, las edades no deben ser iguales");
        }
        categoria.setEdadMinima(5);
        return categoriaRepository.save(categoria);
    }

    public Categoria crearCategoriaDesdeEdad(Categoria categoria){
        //edad y 65;
        if(categoria.getEdadMaxima().equals(categoria.getEdadMinima())){
            throw new BadRequestException("Para crear categoria tipo: DESDE EDAD, las edades no deben ser iguales");
        }
        categoria.setEdadMaxima(65);
        return categoriaRepository.save(categoria);
    }

    public Categoria crearCategoriaEntreEdades(Categoria categoria){
        if(categoria.getEdadMinima() > categoria.getEdadMaxima()){
            throw new BadRequestException("Para crear categoria tipo: ENTRE EDADES, las edad maxima debe ser mayor o igual a la edad minima");
        }
        return categoriaRepository.save(categoria);
    }
    public Categoria getDetalleCategoria(Long id){
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la categoria con ID: " + id));
    }

    public List<Categoria> getListadoCategorias(){
        List<Categoria> listado = categoriaRepository.findByOrderByNombreAsc();
        return listado;
    }

    public void eliminarCategoria(Long id){
        Categoria categoria = getDetalleCategoria(id);
        if(competenciaService.existeReferenciasConCategoria(categoria.getId())){
            throw new BadRequestException("La categoria tiene referencias con competencias y no se puede eliminar");
        }
        categoriaRepository.deleteById(categoria.getId());
    }

    public boolean existeCategoriaPorNombre(String nombreCategoria){
        return categoriaRepository.existsByNombre(nombreCategoria);
    }

    public Integer cantidadTotalCategorias(){
        return categoriaRepository.countCategoriaBy();
    }


}
