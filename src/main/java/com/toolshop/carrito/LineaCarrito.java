package com.toolshop.carrito;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Linea del carrito: un producto y la cantidad de unidades elegidas. Es inmutable. */
public final class LineaCarrito {

    private final Producto producto;
    private final int cantidad;

    LineaCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto producto() {
        return producto;
    }

    public int cantidad() {
        return cantidad;
    }

    /** Subtotal de la linea: precio unitario por cantidad, redondeado a 2 decimales. */
    public BigDecimal subtotal() {
        return producto.precio()
                .multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
