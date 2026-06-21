/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.dao;

/**
 *
 * @author Dell
 */

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAO implements IBaseDAO<Pedido> {

    private final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();

    @Override
    public Pedido guardar(Pedido pedido) {
        String sql = """
                INSERT INTO pedido
                (usuario_id, fecha, estado, total, forma_pago)
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = null;

        try {
            connection = ConexionDB.getConexion();
            connection.setAutoCommit(false);

            pedido.calcularTotal();

            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, pedido.getUsuario().getId());
                statement.setDate(2, Date.valueOf(pedido.getFecha()));
                statement.setString(3, pedido.getEstado().name());
                statement.setDouble(4, pedido.getTotal());
                statement.setString(5, pedido.getFormaPago().name());

                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        pedido.setId(keys.getLong(1));
                    }
                }
            }

            for (var detalle : pedido.getDetalles()) {
                detallePedidoDAO.guardarDetalle(connection, pedido.getId(), detalle);
            }

            connection.commit();
            return pedido;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    throw new RuntimeException("Error al hacer rollback: " + rollbackException.getMessage(), rollbackException);
                }
            }

            throw new RuntimeException("Error al guardar pedido: " + e.getMessage(), e);

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Error al cerrar conexión: " + e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public List<Pedido> listarTodos() {
        String sql = """
                SELECT p.id, p.fecha, p.estado, p.total, p.forma_pago, p.eliminado, p.created_at,
                       u.id AS usuario_id, u.nombre AS usuario_nombre, u.apellido AS usuario_apellido,
                       u.mail AS usuario_mail, u.celular AS usuario_celular, u.rol AS usuario_rol
                FROM pedido p
                INNER JOIN usuario u ON p.usuario_id = u.id
                WHERE p.eliminado = false
                ORDER BY p.id
                """;

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                pedidos.add(mapearPedido(resultSet));
            }

            return pedidos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        String sql = """
                SELECT p.id, p.fecha, p.estado, p.total, p.forma_pago, p.eliminado, p.created_at,
                       u.id AS usuario_id, u.nombre AS usuario_nombre, u.apellido AS usuario_apellido,
                       u.mail AS usuario_mail, u.celular AS usuario_celular, u.rol AS usuario_rol
                FROM pedido p
                INNER JOIN usuario u ON p.usuario_id = u.id
                WHERE p.id = ? AND p.eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Pedido pedido = mapearPedido(resultSet);
                    pedido.setDetalles(detallePedidoDAO.listarPorPedidoId(pedido.getId()));
                    return Optional.of(pedido);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Pedido pedido) {
        String sql = """
                UPDATE pedido
                SET estado = ?, forma_pago = ?, total = ?
                WHERE id = ? AND eliminado = false
                """;

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, pedido.getEstado().name());
            statement.setString(2, pedido.getFormaPago().name());
            statement.setDouble(3, pedido.getTotal());
            statement.setLong(4, pedido.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        Connection connection = null;

        try {
            connection = ConexionDB.getConexion();
            connection.setAutoCommit(false);

            detallePedidoDAO.eliminarPorPedidoId(connection, id);

            String sql = """
                    UPDATE pedido
                    SET eliminado = true
                    WHERE id = ? AND eliminado = false
                    """;

            boolean eliminado;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                eliminado = statement.executeUpdate() > 0;
            }

            connection.commit();
            return eliminado;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    throw new RuntimeException("Error al hacer rollback: " + rollbackException.getMessage(), rollbackException);
                }
            }

            throw new RuntimeException("Error al eliminar pedido: " + e.getMessage(), e);

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Error al cerrar conexión: " + e.getMessage(), e);
                }
            }
        }
    }

    private Pedido mapearPedido(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getLong("usuario_id"));
        usuario.setNombre(resultSet.getString("usuario_nombre"));
        usuario.setApellido(resultSet.getString("usuario_apellido"));
        usuario.setMail(resultSet.getString("usuario_mail"));
        usuario.setCelular(resultSet.getString("usuario_celular"));
        usuario.setRol(Rol.valueOf(resultSet.getString("usuario_rol")));

        Pedido pedido = new Pedido();
        pedido.setId(resultSet.getLong("id"));
        pedido.setUsuario(usuario);
        pedido.setFecha(resultSet.getDate("fecha").toLocalDate());
        pedido.setEstado(Estado.valueOf(resultSet.getString("estado")));
        pedido.setTotal(resultSet.getDouble("total"));
        pedido.setFormaPago(FormaPago.valueOf(resultSet.getString("forma_pago")));
        pedido.setEliminado(resultSet.getBoolean("eliminado"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            pedido.setCreatedAt(createdAt.toLocalDateTime());
        }

        return pedido;
    }
}
