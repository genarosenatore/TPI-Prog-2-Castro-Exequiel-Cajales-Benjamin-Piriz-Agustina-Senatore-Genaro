/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.service;

/**
 *
 * @author genar
 */
import integrado.prog2.dao.CategoriaDAO;
import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.BusinessException;
import integrado.prog2.exception.EntityNotFoundException;
import java.util.List;

public class CategoriaService {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public Categoria crear(String nombre, String descripcion) {
        validarTexto(nombre, "El nombre de la categoría es obligatorio");
        categoriaDAO.buscarPorNombre(nombre).ifPresent(c -> {
            throw new BusinessException("Ya existe una categoría con ese nombre");
        });
        Categoria categoria = new Categoria(nombre.trim(), descripcion);
        return categoriaDAO.guardar(categoria);
    }

    public List<Categoria> listar() {
        return categoriaDAO.listarTodos();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaDAO.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe una categoría activa con id " + id));
    }

    public void editar(Long id, String nombre, String descripcion) {
        Categoria categoria = buscarPorId(id);
        validarTexto(nombre, "El nombre de la categoría es obligatorio");
        categoriaDAO.buscarPorNombre(nombre).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new BusinessException("Ya existe otra categoría con ese nombre");
            }
        });
        categoria.setNombre(nombre.trim());
        categoria.setDescripcion(descripcion);
        if (!categoriaDAO.actualizar(categoria)) {
            throw new EntityNotFoundException("No se pudo actualizar la categoría");
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id);
if (!categoriaDAO.eliminar(id)) {
            throw new EntityNotFoundException("No se pudo eliminar la categoría");
        }
    }

    private void validarTexto(String texto, String mensaje) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new BusinessException(mensaje);
        }
    }
}
