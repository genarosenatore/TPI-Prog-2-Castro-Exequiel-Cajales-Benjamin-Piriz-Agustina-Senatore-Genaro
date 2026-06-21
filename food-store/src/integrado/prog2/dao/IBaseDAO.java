/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.dao;

/**
 *
 * @author genar
 */
import java.util.List;
import java.util.Optional;

public interface IBaseDAO<T> {

    T guardar(T entidad);

    List<T> listarTodos();

    Optional<T> buscarPorId(Long id);

    boolean actualizar(T entidad);

    boolean eliminar(Long id);
}
