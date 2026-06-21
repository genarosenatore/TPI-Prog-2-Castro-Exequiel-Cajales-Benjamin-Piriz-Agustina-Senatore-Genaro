/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.dao;

/**
 *
 * @author execa
 */
import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAO implements IBaseDAO<Producto> {

    @Override
    public Producto guardar(Producto producto) {
        String sql = "INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, producto.getNombre());
            statement.setDouble(2, producto.getPrecio());
            statement.setString(3, producto.getDescripcion());
            statement.setInt(4, producto.getStock());
            statement.setString(5, producto.getImagen());
            statement.setBoolean(6, producto.isDisponible());
            statement.setLong(7, producto.getCategoria().getId());
            statement.executeUpdate();
            
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    producto.setId(keys.getLong(1));
                }
            }
            return producto;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Producto> listarTodos() {
        String sql = "SELECT p.id, p.nombre, p.precio, p.descripcion, p.stock, p.imagen, p.disponible, p.eliminado, p.created_at, " +
                     "c.id AS categoria_id, c.nombre AS categoria_nombre, c.descripcion AS categoria_descripcion " +
                     "FROM producto p " +
                     "INNER JOIN categoria c ON p.categoria_id = c.id " +
                     "WHERE p.eliminado = false ORDER BY p.id";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                productos.add(mapearProducto(resultSet));
            }
            return productos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos: " + e.getMessage(), e);
        }
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        String sql = "SELECT p.id, p.nombre, p.precio, p.descripcion, p.stock, p.imagen, p.disponible, p.eliminado, p.created_at, " +
                     "c.id AS categoria_id, c.nombre AS categoria_nombre, c.descripcion AS categoria_descripcion " +
                     "FROM producto p " +
                     "INNER JOIN categoria c ON p.categoria_id = c.id " +
                     "WHERE p.eliminado = false AND p.categoria_id = ? ORDER BY p.id";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setLong(1, categoriaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    productos.add(mapearProducto(resultSet));
                }
            }
            return productos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos por categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        String sql = "SELECT p.id, p.nombre, p.precio, p.descripcion, p.stock, p.imagen, p.disponible, p.eliminado, p.created_at, " +
                     "c.id AS categoria_id, c.nombre AS categoria_nombre, c.descripcion AS categoria_descripcion " +
                     "FROM producto p " +
                     "INNER JOIN categoria c ON p.categoria_id = c.id " +
                     "WHERE p.id = ? AND p.eliminado = false";
        
        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearProducto(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, imagen = ?, disponible = ?, categoria_id = ? " +
                     "WHERE id = ? AND eliminado = false";
        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, producto.getNombre());
            statement.setDouble(2, producto.getPrecio());
            statement.setString(3, producto.getDescripcion());
            statement.setInt(4, producto.getStock());
            statement.setString(5, producto.getImagen());
            statement.setBoolean(6, producto.isDisponible());
            statement.setLong(7, producto.getCategoria().getId());
            statement.setLong(8, producto.getId());
            
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = "UPDATE producto SET eliminado = true WHERE id = ? AND eliminado = false";
        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }

    private Producto mapearProducto(ResultSet resultSet) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(resultSet.getLong("categoria_id"));
        categoria.setNombre(resultSet.getString("categoria_nombre"));
        categoria.setDescripcion(resultSet.getString("categoria_descripcion"));
        
        Producto producto = new Producto();
        producto.setId(resultSet.getLong("id"));
        producto.setNombre(resultSet.getString("nombre"));
        producto.setPrecio(resultSet.getDouble("precio"));
        producto.setDescripcion(resultSet.getString("descripcion"));
        producto.setStock(resultSet.getInt("stock"));
        producto.setImagen(resultSet.getString("imagen"));
        producto.setDisponible(resultSet.getBoolean("disponible"));
        producto.setEliminado(resultSet.getBoolean("eliminado"));
        producto.setCategoria(categoria);
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            producto.setCreatedAt(createdAt.toLocalDateTime());
        }
        return producto;
    }
}