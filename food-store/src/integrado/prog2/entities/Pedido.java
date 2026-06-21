/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.entities;

/**
 *
 * @author Dell
 */

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.interfaces.Calculable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
    }

    public Pedido(Usuario usuario, FormaPago formaPago) {
        this();
        this.usuario = usuario;
        this.formaPago = formaPago;
    }

    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        if (precioUnitario == null || precioUnitario < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }

        Double subtotal = cantidad * precioUnitario;
        DetallePedido detalle = new DetallePedido(cantidad, subtotal, producto);
        detalles.add(detalle);
        calcularTotal();
    }

    public Optional<DetallePedido> findeDetallePedidoByProducto(Producto producto) {
        if (producto == null || producto.getId() == null) {
            return Optional.empty();
        }

        return detalles.stream()
                .filter(detalle -> detalle.getProducto() != null)
                .filter(detalle -> producto.getId().equals(detalle.getProducto().getId()))
                .findFirst();
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        findeDetallePedidoByProducto(producto).ifPresent(detalles::remove);
        calcularTotal();
    }

    @Override
    public void calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
        calcularTotal();
    }

    @Override
    public String toString() {
        String nombreUsuario = usuario != null
                ? usuario.getNombre() + " " + usuario.getApellido()
                : "Sin usuario";

        return "Pedido{" +
                "id=" + getId() +
                ", usuario=" + nombreUsuario +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", formaPago=" + formaPago +
                ", total=" + total +
                '}';
    }
}
