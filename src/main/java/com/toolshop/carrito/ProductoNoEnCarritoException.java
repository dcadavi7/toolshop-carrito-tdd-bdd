package com.toolshop.carrito;

/** Se lanza al intentar cambiar la cantidad de un producto que no esta en el carrito. */
public class ProductoNoEnCarritoException extends RuntimeException {

    public ProductoNoEnCarritoException(String nombreProducto) {
        super("El producto no esta en el carrito: " + nombreProducto);
    }
}
