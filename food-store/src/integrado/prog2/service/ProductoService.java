/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.service;

/**
 *
 * @author execa
 */
import integrado.prog2.dao.ProductoDAO;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.BusinessException;
import integrado.prog2.exception.EntityNotFoundException;
import java.util.List;

public class ProductoService {
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaService categoriaService = new CategoriaService();

    public Producto crear(String nombre, String descripcion, Double precio, int stock, String imagen, boolean disponible, Long categoriaId) {
        validarNombre(nombre);
        validarPrecioYStock(precio, stock);
        
        // Verifica que la categoría exista y esté activa (si no, lanza EntityNotFoundException)
        Categoria categoria = categoriaService.buscarPorId(categoriaId);
        
        Producto producto = new Producto(nombre.trim(), precio, descripcion, stock, imagen, disponible, categoria);
        return productoDAO.guardar(producto);
    }

    public List<Producto> listar() {
        return productoDAO.listarTodos();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        categoriaService.buscarPorId(categoriaId); // Valida que la categoría exista primero
        return productoDAO.listarPorCategoria(categoriaId);
    }

    public Producto buscarPorId(Long id) {
        return productoDAO.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un producto activo con id " + id));
    }

    public void editar(Producto producto) {
        validarNombre(producto.getNombre());
        validarPrecioYStock(producto.getPrecio(), producto.getStock());
        
        // Valida la existencia de la categoría asociada
        categoriaService.buscarPorId(producto.getCategoria().getId());
        
        // Valida que el producto exista antes de actualizar
        buscarPorId(producto.getId());
        
        if (!productoDAO.actualizar(producto)) {
            throw new EntityNotFoundException("No se pudo actualizar el producto");
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id); // Valida existencia
        if (!productoDAO.eliminar(id)) {
            throw new EntityNotFoundException("No se pudo eliminar el producto");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre del producto es obligatorio");
        }
    }

    private void validarPrecioYStock(Double precio, int stock) {
        if (precio == null || precio < 0) {
            throw new BusinessException("El precio no puede ser negativo");
        }
        if (stock < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }
    }
}