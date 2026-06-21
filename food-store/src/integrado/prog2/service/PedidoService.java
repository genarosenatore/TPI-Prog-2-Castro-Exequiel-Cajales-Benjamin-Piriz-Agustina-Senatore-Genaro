/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.service;

/**
 *
 * @author Dell
 */

import integrado.prog2.dao.PedidoDAO;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.BusinessException;
import integrado.prog2.exception.EntityNotFoundException;

import java.util.List;
import java.util.Map;

public class PedidoService {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final UsuarioService usuarioService = new UsuarioService();
    private final ProductoService productoService = new ProductoService();

    public Pedido crearPedido(Long usuarioId, FormaPago formaPago, Map<Long, Integer> productosCantidades) {
        if (usuarioId == null) {
            throw new BusinessException("El usuario es obligatorio para crear un pedido");
        }

        if (formaPago == null) {
            throw new BusinessException("La forma de pago es obligatoria");
        }

        if (productosCantidades == null || productosCantidades.isEmpty()) {
            throw new BusinessException("El pedido debe tener al menos un detalle");
        }

        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        Pedido pedido = new Pedido(usuario, formaPago);
        pedido.setEstado(Estado.PENDIENTE);

        for (Map.Entry<Long, Integer> entry : productosCantidades.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();

            if (cantidad == null || cantidad <= 0) {
                throw new BusinessException("La cantidad debe ser mayor a cero");
            }

            Producto producto = productoService.buscarPorId(productoId);

            if (!producto.isDisponible()) {
                throw new BusinessException("El producto no estÃ¡ disponible: " + producto.getNombre());
            }

            pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);
        }

        pedido.calcularTotal();

        return pedidoDAO.guardar(pedido);
    }

    public List<Pedido> listar() {
        return pedidoDAO.listarTodos();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoDAO.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un pedido activo con id " + id));
    }

    public void actualizarEstadoFormaPago(Long id, Estado estado, FormaPago formaPago) {
        Pedido pedido = buscarPorId(id);

        if (estado == null) {
            throw new BusinessException("El estado es obligatorio");
        }

        if (formaPago == null) {
            throw new BusinessException("La forma de pago es obligatoria");
        }

        pedido.setEstado(estado);
        pedido.setFormaPago(formaPago);

        if (!pedidoDAO.actualizar(pedido)) {
            throw new EntityNotFoundException("No se pudo actualizar el pedido");
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id);

        if (!pedidoDAO.eliminar(id)) {
            throw new EntityNotFoundException("No se pudo eliminar el pedido");
        }
    }
}
