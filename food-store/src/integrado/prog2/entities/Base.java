/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.entities;

/**
 *
 * @author genar
 */

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Base {

    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    public Base() {
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    public Base(Long id, boolean eliminado, LocalDateTime createdAt) {
        this.id = id;
        this.eliminado = eliminado;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Base base)) {
            return false;
        }

        return id != null && Objects.equals(id, base.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
