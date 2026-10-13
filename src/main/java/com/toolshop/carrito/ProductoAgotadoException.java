package com.toolshop.carrito;

/** Se lanza al intentar agregar al carrito un producto sin existencias (HU02-CA04). */
public class ProductoAgotadoException extends RuntimeException {

    public ProductoAgotadoException(String nombreProducto) {
        super("El producto esta agotado: " + nombreProducto);
    }
}
