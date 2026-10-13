package com.toolshop.carrito;


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
}
