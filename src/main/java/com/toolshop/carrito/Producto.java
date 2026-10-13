package com.toolshop.carrito;

import java.math.BigDecimal;

/**
 * Producto del catalogo de Toolshop.
 *
 * @param nombre          nombre unico del producto
 * @param precio          precio unitario en dolares
 * @param stock           unidades disponibles
 * @param calificacionCo2 calificacion de huella de carbono (A a E)
 */
public record Producto(String nombre, BigDecimal precio, int stock, String calificacionCo2) {
    /** Un producto se puede agregar al carrito solo si tiene existencias. */
    public boolean estaDisponible() {
        return stock > 0;
    }
}
