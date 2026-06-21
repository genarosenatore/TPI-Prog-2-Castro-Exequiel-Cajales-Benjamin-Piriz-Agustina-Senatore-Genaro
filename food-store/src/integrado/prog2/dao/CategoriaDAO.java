/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.dao;

/**
 *
 * @author genar
 */
import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDAO implements IBaseDAO<Categoria> {

    @Override
    public Categoria guardar(Categoria categoria) {
        String sql = """
                INSERT INTO categoria (nombre, descripcion)
                VALUES (?, ?)
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, categoria.getNombre());
            statement.setString(2, categoria.getDescripcion());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    categoria.setId(keys.getLong(1));
                }
            }

            return categoria;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Categoria> listarTodos() {
        String sql = """
                SELECT id, nombre, descripcion, eliminado, created_at
                FROM categoria
                WHERE eliminado = false
                ORDER BY id
                """;

        List<Categoria> categorias = new ArrayList<>();

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categorias.add(mapearCategoria(resultSet));
            }

            return categorias;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar categorías: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        String sql = """
                SELECT id, nombre, descripcion, eliminado, created_at
                FROM categoria
                WHERE id = ? AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearCategoria(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría: " + e.getMessage(), e);
        }
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        String sql = """
                SELECT id, nombre, descripcion, eliminado, created_at
                FROM categoria
                WHERE LOWER(nombre) = LOWER(?) AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nombre);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearCategoria(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría por nombre: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Categoria categoria) {
        String sql = """
                UPDATE categoria
                SET nombre = ?, descripcion = ?
                WHERE id = ? AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoria.getNombre());
            statement.setString(2, categoria.getDescripcion());
            statement.setLong(3, categoria.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = """
                UPDATE categoria
                SET eliminado = true
                WHERE id = ? AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar categoría: " + e.getMessage(), e);
        }
    }

    private Categoria mapearCategoria(ResultSet resultSet) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(resultSet.getLong("id"));
        categoria.setNombre(resultSet.getString("nombre"));
        categoria.setDescripcion(resultSet.getString("descripcion"));
        categoria.setEliminado(resultSet.getBoolean("eliminado"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            categoria.setCreatedAt(createdAt.toLocalDateTime());
        }

        return categoria;
    }
}
