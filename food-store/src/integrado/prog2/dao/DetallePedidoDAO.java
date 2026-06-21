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
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDAO {

    public DetallePedido guardarDetalle(Connection connection, Long pedidoId, DetallePedido detalle) throws SQLException {
        String sql = """
                INSERT INTO detalle_pedido
                (pedido_id, producto_id, cantidad, subtotal)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, pedidoId);
            statement.setLong(2, detalle.getProducto().getId());
            statement.setInt(3, detalle.getCantidad());
            statement.setDouble(4, detalle.getSubtotal());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    detalle.setId(keys.getLong(1));
                }
            }

            return detalle;
        }
    }

    public List<DetallePedido> listarPorPedidoId(Long pedidoId) {
        String sql = """
                SELECT dp.id, dp.cantidad, dp.subtotal, dp.eliminado, dp.created_at,
                       p.id AS producto_id, p.nombre AS producto_nombre, p.precio AS producto_precio,
                       p.descripcion AS producto_descripcion, p.stock AS producto_stock,
                       p.imagen AS producto_imagen, p.disponible AS producto_disponible,
                       c.id AS categoria_id, c.nombre AS categoria_nombre, c.descripcion AS categoria_descripcion
                FROM detalle_pedido dp
                INNER JOIN producto p ON dp.producto_id = p.id
                INNER JOIN categoria c ON p.categoria_id = c.id
                WHERE dp.pedido_id = ? AND dp.eliminado = false
                ORDER BY dp.id
                """;

        List<DetallePedido> detalles = new ArrayList<>();

        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, pedidoId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    detalles.add(mapearDetalle(resultSet));
                }
            }

            return detalles;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar detalles del pedido: " + e.getMessage(), e);
        }
    }

    public void eliminarPorPedidoId(Connection connection, Long pedidoId) throws SQLException {
        String sql = """
                UPDATE detalle_pedido
                SET eliminado = true
                WHERE pedido_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, pedidoId);
            statement.executeUpdate();
        }
    }

    private DetallePedido mapearDetalle(ResultSet resultSet) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(resultSet.getLong("categoria_id"));
        categoria.setNombre(resultSet.getString("categoria_nombre"));
        categoria.setDescripcion(resultSet.getString("categoria_descripcion"));

        Producto producto = new Producto();
        producto.setId(resultSet.getLong("producto_id"));
        producto.setNombre(resultSet.getString("producto_nombre"));
        producto.setPrecio(resultSet.getDouble("producto_precio"));
        producto.setDescripcion(resultSet.getString("producto_descripcion"));
        producto.setStock(resultSet.getInt("producto_stock"));
        producto.setImagen(resultSet.getString("producto_imagen"));
        producto.setDisponible(resultSet.getBoolean("producto_disponible"));
        producto.setCategoria(categoria);

        DetallePedido detalle = new DetallePedido();
        detalle.setId(resultSet.getLong("id"));
        detalle.setCantidad(resultSet.getInt("cantidad"));
        detalle.setSubtotal(resultSet.getDouble("subtotal"));
        detalle.setProducto(producto);
        detalle.setEliminado(resultSet.getBoolean("eliminado"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            detalle.setCreatedAt(createdAt.toLocalDateTime());
        }

        return detalle;
    }
}
