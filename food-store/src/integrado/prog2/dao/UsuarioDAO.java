/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.dao;

/**
 *
 * @author benja_c
 */

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements IBaseDAO<Usuario> {

    @Override
    public Usuario guardar(Usuario usuario) {
        String sql = """
                INSERT INTO usuario
                (nombre, apellido, mail, celular, contrasenia, rol)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setString(3, usuario.getMail());
            statement.setString(4, usuario.getCelular());
            statement.setString(5, usuario.getContrasenia());
            statement.setString(6, usuario.getRol().name());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setId(keys.getLong(1));
                }
            }

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = """
                SELECT id, nombre, apellido, mail, celular, contrasenia, rol, eliminado, created_at
                FROM usuario
                WHERE eliminado = false
                ORDER BY id
                """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                usuarios.add(mapearUsuario(resultSet));
            }

            return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = """
                SELECT id, nombre, apellido, mail, celular, contrasenia, rol, eliminado, created_at
                FROM usuario
                WHERE id = ? AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearUsuario(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    public Optional<Usuario> buscarPorMail(String mail) {
        String sql = """
                SELECT id, nombre, apellido, mail, celular, contrasenia, rol, eliminado, created_at
                FROM usuario
                WHERE LOWER(mail) = LOWER(?) AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, mail);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearUsuario(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por mail: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = """
                UPDATE usuario
                SET nombre = ?, apellido = ?, mail = ?, celular = ?, contrasenia = ?, rol = ?
                WHERE id = ? AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setString(3, usuario.getMail());
            statement.setString(4, usuario.getCelular());
            statement.setString(5, usuario.getContrasenia());
            statement.setString(6, usuario.getRol().name());
            statement.setLong(7, usuario.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = """
                UPDATE usuario
                SET eliminado = true
                WHERE id = ? AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    private Usuario mapearUsuario(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getLong("id"));
        usuario.setNombre(resultSet.getString("nombre"));
        usuario.setApellido(resultSet.getString("apellido"));
        usuario.setMail(resultSet.getString("mail"));
        usuario.setCelular(resultSet.getString("celular"));
        usuario.setContrasenia(resultSet.getString("contrasenia"));
        usuario.setRol(Rol.valueOf(resultSet.getString("rol")));
        usuario.setEliminado(resultSet.getBoolean("eliminado"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            usuario.setCreatedAt(createdAt.toLocalDateTime());
        }

        return usuario;
    }
}
