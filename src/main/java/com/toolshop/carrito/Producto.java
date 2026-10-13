package com.toolshop.carrito;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Producto del catalogo de Toolshop.
 *
 * @param nombre          nombre unico del producto
 * @param precio          precio unitario en dolares
 * @param stock           unidades disponibles
 * @param calificacionCo2 calificacion de huella de carbono (A a E)
 */
public record Producto(String nombre, BigDecimal precio, int stock, String calificacionCo2) {
    public Producto {
        Objects.requireNonNull(nombre, "El nombre es obligatorio");
        Objects.requireNonNull(precio, "El precio es obligatorio");
        if (precio.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    /** Un producto se puede agregar al carrito solo si tiene existencias. */
    public boolean estaDisponible() {
        return stock > 0;
    }

    /** En Toolshop, los productos con calificacion CO2 A o B se consideran ecologicos. */
    public boolean esEcologico() {
        return "A".equalsIgnoreCase(calificacionCo2) || "B".equalsIgnoreCase(calificacionCo2);
    }
}
