/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.ui;

/**
 *
 * @author genar
 */

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.enums.Rol;
import integrado.prog2.service.CategoriaService;
import integrado.prog2.service.PedidoService;
import integrado.prog2.service.ProductoService;
import integrado.prog2.service.UsuarioService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuConsola {

    private final Scanner scanner = new Scanner(System.in);

    private final CategoriaService categoriaService = new CategoriaService();
    private final ProductoService productoService = new ProductoService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final PedidoService pedidoService = new PedidoService();

    public void iniciar() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");

            opcion = leerEntero("Seleccione: ");

            try {
                switch (opcion) {
                    case 1 -> menuCategorias();
                    case 2 -> menuProductos();
                    case 3 -> menuUsuarios();
                    case 4 -> menuPedidos();
                    case 0 -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void menuCategorias() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== CATEGORÍAS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione: ");

            try {
                switch (opcion) {
                    case 1 -> listarCategorias();
                    case 2 -> crearCategoria();
                    case 3 -> editarCategoria();
                    case 4 -> eliminarCategoria();
                    case 0 -> { }
                    default -> System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void listarCategorias() {
        List<Categoria> categorias = categoriaService.listar();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorías cargadas");
            return;
        }

        categorias.forEach(System.out::println);
    }

    private void crearCategoria() {
        String nombre = leerTextoObligatorio("Nombre: ");
        String descripcion = leerTexto("Descripción: ");

        Categoria categoria = categoriaService.crear(nombre, descripcion);
        System.out.println("Categoría creada con id: " + categoria.getId());
    }

    private void editarCategoria() {
        listarCategorias();

        Long id = leerLong("ID de categoría a editar: ");
        Categoria categoria = categoriaService.buscarPorId(id);

        String nombre = leerTextoOpcional("Nuevo nombre [" + categoria.getNombre() + "]: ", categoria.getNombre());
        String descripcion = leerTextoOpcional("Nueva descripción [" + categoria.getDescripcion() + "]: ", categoria.getDescripcion());

        categoriaService.editar(id, nombre, descripcion);
        System.out.println("Categoría actualizada correctamente");
    }

    private void eliminarCategoria() {
        listarCategorias();

        Long id = leerLong("ID de categoría a eliminar: ");

        if (confirmar("¿Confirmar eliminación? S/N: ")) {
            categoriaService.eliminar(id);
            System.out.println("Categoría eliminada correctamente");
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void menuProductos() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== PRODUCTOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("5. Listar por categoría");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione: ");

            try {
                switch (opcion) {
                    case 1 -> listarProductos();
                    case 2 -> crearProducto();
                    case 3 -> editarProducto();
                    case 4 -> eliminarProducto();
                    case 5 -> listarProductosPorCategoria();
                    case 0 -> { }
                    default -> System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void listarProductos() {
        List<Producto> productos = productoService.listar();

        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados");
            return;
        }

        productos.forEach(System.out::println);
    }

    private void crearProducto() {
        String nombre = leerTextoObligatorio("Nombre: ");
        String descripcion = leerTexto("Descripción: ");
        Double precio = leerDouble("Precio: ");
        int stock = leerEntero("Stock: ");
        String imagen = leerTexto("Imagen: ");
        boolean disponible = leerBoolean("Disponible S/N: ");

        listarCategorias();
        Long categoriaId = leerLong("ID de categoría: ");

        Producto producto = productoService.crear(nombre, descripcion, precio, stock, imagen, disponible, categoriaId);
        System.out.println("Producto creado con id: " + producto.getId());
    }

    private void editarProducto() {
        listarProductos();

        Long id = leerLong("ID de producto a editar: ");
        Producto producto = productoService.buscarPorId(id);

        String nombre = leerTextoOpcional("Nuevo nombre [" + producto.getNombre() + "]: ", producto.getNombre());
        String descripcion = leerTextoOpcional("Nueva descripción [" + producto.getDescripcion() + "]: ", producto.getDescripcion());
        Double precio = leerDoubleOpcional("Nuevo precio [" + producto.getPrecio() + "]: ", producto.getPrecio());
        int stock = leerEnteroOpcional("Nuevo stock [" + producto.getStock() + "]: ", producto.getStock());
        String imagen = leerTextoOpcional("Nueva imagen [" + producto.getImagen() + "]: ", producto.getImagen());
        boolean disponible = leerBooleanOpcional("Disponible [" + (producto.isDisponible() ? "S" : "N") + "] S/N/Enter: ", producto.isDisponible());

        listarCategorias();
        String categoriaTexto = leerTexto("Nuevo ID de categoría [" + producto.getCategoria().getId() + "]: ");
        Long categoriaId = categoriaTexto.isBlank()
                ? producto.getCategoria().getId()
                : Long.parseLong(categoriaTexto);

        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setImagen(imagen);
        producto.setDisponible(disponible);
        producto.setCategoria(categoria);

        productoService.editar(producto);
        System.out.println("Producto actualizado correctamente");
    }

    private void eliminarProducto() {
        listarProductos();

        Long id = leerLong("ID de producto a eliminar: ");

        if (confirmar("¿Confirmar eliminación? S/N: ")) {
            productoService.eliminar(id);
            System.out.println("Producto eliminado correctamente");
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void listarProductosPorCategoria() {
        listarCategorias();

        Long categoriaId = leerLong("ID de categoría: ");
        List<Producto> productos = productoService.listarPorCategoria(categoriaId);

        if (productos.isEmpty()) {
            System.out.println("No hay productos para esa categoría");
            return;
        }

        productos.forEach(System.out::println);
    }

    private void menuUsuarios() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== USUARIOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione: ");

            try {
                switch (opcion) {
                    case 1 -> listarUsuarios();
                    case 2 -> crearUsuario();
                    case 3 -> editarUsuario();
                    case 4 -> eliminarUsuario();
                    case 0 -> { }
                    default -> System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listar();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados");
            return;
        }

        usuarios.forEach(System.out::println);
    }

    private void crearUsuario() {
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String mail = leerTextoObligatorio("Mail: ");
        String celular = leerTexto("Celular: ");
        String contrasenia = leerTexto("Contraseña: ");
        Rol rol = leerEnum(Rol.class, "Rol");

        Usuario usuario = usuarioService.crear(nombre, apellido, mail, celular, contrasenia, rol);
        System.out.println("Usuario creado con id: " + usuario.getId());
    }

    private void editarUsuario() {
        listarUsuarios();

        Long id = leerLong("ID de usuario a editar: ");
        Usuario usuario = usuarioService.buscarPorId(id);

        String nombre = leerTextoOpcional("Nuevo nombre [" + usuario.getNombre() + "]: ", usuario.getNombre());
        String apellido = leerTextoOpcional("Nuevo apellido [" + usuario.getApellido() + "]: ", usuario.getApellido());
        String mail = leerTextoOpcional("Nuevo mail [" + usuario.getMail() + "]: ", usuario.getMail());
        String celular = leerTextoOpcional("Nuevo celular [" + usuario.getCelular() + "]: ", usuario.getCelular());
        String contrasenia = leerTextoOpcional("Nueva contraseña [mantener]: ", usuario.getContrasenia());
        Rol rol = leerEnumOpcional(Rol.class, "Rol", usuario.getRol());

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setCelular(celular);
        usuario.setContrasenia(contrasenia);
        usuario.setRol(rol);

        usuarioService.editar(usuario);
        System.out.println("Usuario actualizado correctamente");
    }

    private void eliminarUsuario() {
        listarUsuarios();

        Long id = leerLong("ID de usuario a eliminar: ");

        if (confirmar("¿Confirmar eliminación? S/N: ")) {
            usuarioService.eliminar(id);
            System.out.println("Usuario eliminado correctamente");
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void menuPedidos() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== PEDIDOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear pedido con detalles");
            System.out.println("3. Actualizar estado / forma de pago");
            System.out.println("4. Eliminar");
            System.out.println("5. Ver detalle de pedido");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione: ");

            try {
                switch (opcion) {
                    case 1 -> listarPedidos();
                    case 2 -> crearPedido();
                    case 3 -> actualizarPedido();
                    case 4 -> eliminarPedido();
                    case 5 -> verDetallePedido();
                    case 0 -> { }
                    default -> System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listar();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos cargados");
            return;
        }

        pedidos.forEach(System.out::println);
    }

    private void crearPedido() {
        listarUsuarios();
        Long usuarioId = leerLong("ID de usuario: ");

        FormaPago formaPago = leerEnum(FormaPago.class, "Forma de pago");

        Map<Long, Integer> productosCantidades = new LinkedHashMap<>();

        boolean seguir;

        do {
            listarProductos();
            Long productoId = leerLong("ID de producto: ");
            int cantidad = leerEntero("Cantidad: ");

            productosCantidades.put(productoId, cantidad);

            seguir = confirmar("¿Agregar otro producto? S/N: ");
        } while (seguir);

        Pedido pedido = pedidoService.crearPedido(usuarioId, formaPago, productosCantidades);
        System.out.println("Pedido creado con id: " + pedido.getId());
        System.out.println("Total: $" + pedido.getTotal());
    }

    private void actualizarPedido() {
        listarPedidos();

        Long id = leerLong("ID de pedido a actualizar: ");

        Estado estado = leerEnum(Estado.class, "Estado");
        FormaPago formaPago = leerEnum(FormaPago.class, "Forma de pago");

        pedidoService.actualizarEstadoFormaPago(id, estado, formaPago);
        System.out.println("Pedido actualizado correctamente");
    }

    private void eliminarPedido() {
        listarPedidos();

        Long id = leerLong("ID de pedido a eliminar: ");

        if (confirmar("¿Confirmar eliminación? S/N: ")) {
            pedidoService.eliminar(id);
            System.out.println("Pedido eliminado correctamente");
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void verDetallePedido() {
        listarPedidos();

        Long id = leerLong("ID de pedido: ");
        Pedido pedido = pedidoService.buscarPorId(id);

        System.out.println(pedido);

        if (pedido.getDetalles().isEmpty()) {
            System.out.println("El pedido no tiene detalles");
            return;
        }

        pedido.getDetalles().forEach(System.out::println);
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido");
            }
        }
    }

    private int leerEnteroOpcional(String mensaje, int valorActual) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();

                if (input.isBlank()) {
                    return valorActual;
                }

                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido");
            }
        }
    }

    private Long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un ID válido");
            }
        }
    }

    private Double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido");
            }
        }
    }

    private Double leerDoubleOpcional(String mensaje, Double valorActual) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();

                if (input.isBlank()) {
                    return valorActual;
                }

                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine();

            if (!texto.trim().isEmpty()) {
                return texto;
            }

            System.out.println("Este campo es obligatorio");
        }
    }

    private String leerTextoOpcional(String mensaje, String valorActual) {
        System.out.print(mensaje);
        String texto = scanner.nextLine();

        if (texto.trim().isEmpty()) {
            return valorActual;
        }

        return texto;
    }

    private boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("S")) return true;
            if (input.equals("N")) return false;

            System.out.println("Debe ingresar S o N");
        }
    }

    private boolean leerBooleanOpcional(String mensaje, boolean valorActual) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.isBlank()) return valorActual;
            if (input.equals("S")) return true;
            if (input.equals("N")) return false;

            System.out.println("Debe ingresar S, N o Enter");
        }
    }

    private boolean confirmar(String mensaje) {
        return leerBoolean(mensaje);
    }

    private <E extends Enum<E>> E leerEnum(Class<E> enumClass, String titulo) {
        E[] valores = enumClass.getEnumConstants();

        while (true) {
            System.out.println(titulo + ":");

            for (int i = 0; i < valores.length; i++) {
                System.out.println((i + 1) + ". " + valores[i]);
            }

            int opcion = leerEntero("Seleccione: ");

            if (opcion >= 1 && opcion <= valores.length) {
                return valores[opcion - 1];
            }

            System.out.println("Opción inválida");
        }
    }

    private <E extends Enum<E>> E leerEnumOpcional(Class<E> enumClass, String titulo, E valorActual) {
        E[] valores = enumClass.getEnumConstants();

        while (true) {
            System.out.println(titulo + " actual: " + valorActual);

            for (int i = 0; i < valores.length; i++) {
                System.out.println((i + 1) + ". " + valores[i]);
            }

            System.out.print("Seleccione o Enter para mantener: ");
            String input = scanner.nextLine().trim();

            if (input.isBlank()) {
                return valorActual;
            }

            try {
                int opcion = Integer.parseInt(input);

                if (opcion >= 1 && opcion <= valores.length) {
                    return valores[opcion - 1];
                }

                System.out.println("Opción inválida");

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido");
            }
        }
    }
}