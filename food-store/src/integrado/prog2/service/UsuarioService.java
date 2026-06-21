/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.service;

/**
 *
 * @author benja_c
 */

import integrado.prog2.dao.UsuarioDAO;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.BusinessException;
import integrado.prog2.exception.EntityNotFoundException;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario crear(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        validarUsuario(nombre, apellido, mail, rol);

        usuarioDAO.buscarPorMail(mail).ifPresent(u -> {
            throw new BusinessException("Ya existe un usuario con ese mail");
        });

        Usuario usuario = new Usuario(
                nombre.trim(),
                apellido.trim(),
                mail.trim(),
                celular,
                contrasenia,
                rol
        );

        return usuarioDAO.guardar(usuario);
    }

    public List<Usuario> listar() {
        return usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario activo con id " + id));
    }

    public void editar(Usuario usuario) {
        validarUsuario(usuario.getNombre(), usuario.getApellido(), usuario.getMail(), usuario.getRol());

        usuarioDAO.buscarPorMail(usuario.getMail()).ifPresent(u -> {
            if (!u.getId().equals(usuario.getId())) {
                throw new BusinessException("Ya existe otro usuario con ese mail");
            }
        });

        if (!usuarioDAO.actualizar(usuario)) {
            throw new EntityNotFoundException("No se pudo actualizar el usuario");
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id);

        if (!usuarioDAO.eliminar(id)) {
            throw new EntityNotFoundException("No se pudo eliminar el usuario");
        }
    }

    private void validarUsuario(String nombre, String apellido, String mail, Rol rol) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre del usuario es obligatorio");
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            throw new BusinessException("El apellido del usuario es obligatorio");
        }

        if (mail == null || mail.trim().isEmpty()) {
            throw new BusinessException("El mail del usuario es obligatorio");
        }

        if (!mail.contains("@")) {
            throw new BusinessException("El mail no tiene un formato vÃ¡lido");
        }

        if (rol == null) {
            throw new BusinessException("El rol es obligatorio");
        }
    }
}
