package backendAdministradorCompetenciasFutbolisticas.Controller;

import backendAdministradorCompetenciasFutbolisticas.Dtos.CategoriaDto;
import backendAdministradorCompetenciasFutbolisticas.Dtos.Mensaje;
import backendAdministradorCompetenciasFutbolisticas.Entity.Categoria;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.BadRequestException;
import backendAdministradorCompetenciasFutbolisticas.Excepciones.InvalidDataException;
import backendAdministradorCompetenciasFutbolisticas.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin("*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CategoriaDto nuevaCategoria, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        if(categoriaService.existeCategoriaPorNombre(nuevaCategoria.getNombre())){
            throw new BadRequestException("Ya existe una categoria con el nombre: " + nuevaCategoria.getNombre());
        }
        Categoria categoria = new Categoria(nuevaCategoria.getNombre(), nuevaCategoria.getDescripcion(), nuevaCategoria.getTipo(), nuevaCategoria.getEdadMinima(), nuevaCategoria.getEdadMaxima());
        categoriaService.guardarCategoria(categoria);
        return new ResponseEntity<>(new Mensaje("Nueva Categoria guardada correctamente"), HttpStatus.CREATED);
    }

    @GetMapping("/detalle/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<Categoria> detelleCategoria(@PathVariable("id") Long id){
        Categoria categoria = categoriaService.getDetalleCategoria(id);
        return new ResponseEntity<>(categoria,HttpStatus.OK);
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<Categoria> editarCategoria(@PathVariable ("id") Long id, @Valid @RequestBody CategoriaDto editarCategoria, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidDataException(bindingResult);
        }
        Categoria categoriaObtenida = categoriaService.getDetalleCategoria(id);
        if(!categoriaObtenida.getNombre().equals(editarCategoria.getNombre()) && categoriaService.existeCategoriaPorNombre(editarCategoria.getNombre())){
            throw new BadRequestException("Ya existe una categoria con el nombre: " + editarCategoria.getNombre());
        }
        categoriaObtenida.setNombre(editarCategoria.getNombre());
        categoriaObtenida.setDescripcion(editarCategoria.getDescripcion());
        categoriaObtenida.setTipo(editarCategoria.getTipo());
        categoriaObtenida.setEdadMinima(editarCategoria.getEdadMinima());
        categoriaObtenida.setEdadMaxima(editarCategoria.getEdadMaxima());
        Categoria categoriaEditada = categoriaService.guardarCategoria(categoriaObtenida);
        return new ResponseEntity<>(categoriaEditada,HttpStatus.OK);
    }

    @GetMapping("/listado")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<List<Categoria>> listadoDeCategorias(){
        List<Categoria> listado = categoriaService.getListadoCategorias();
        return new ResponseEntity<>(listado, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<?> eliminarCategoria(@PathVariable ("id") Long id){
        categoriaService.eliminarCategoria(id);
        return new ResponseEntity<>(new Mensaje("Categoria eliminada correctamente"),HttpStatus.OK);
    }

    @GetMapping("/cantidad-total")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENCARGADO_DE_TORNEOS')")
    public ResponseEntity<Integer> cantidadTotalCategorias(){
        Integer cantidad = categoriaService.cantidadTotalCategorias();
        return new ResponseEntity<>(cantidad,HttpStatus.OK);
    }

}
