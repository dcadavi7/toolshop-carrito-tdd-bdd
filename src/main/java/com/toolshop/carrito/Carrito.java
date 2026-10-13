package com.toolshop.carrito;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Carrito de compras de Toolshop (HU-02).
 * Las lineas se guardan por nombre de producto y conservan el orden en que se agregaron.
 */
public class Carrito {

    /** Cantidad minima por linea. Valores menores se ajustan a este minimo. */
    public static final int CANTIDAD_MINIMA = 1;

    /** Cantidad maxima por linea. Valores mayores se ajustan a este maximo. */
    public static final int CANTIDAD_MAXIMA = 99;

    private static final BigDecimal CERO = BigDecimal.ZERO.setScale(2);

    private final Map<String, LineaCarrito> lineas = new LinkedHashMap<>();

    /**
     * Agrega unidades de un producto; si ya estaba en el carrito, se acumulan (HU02-CA01).
     */
    public void agregar(Producto producto, int cantidad) {
        Objects.requireNonNull(producto, "El producto es obligatorio");
        if (!producto.estaDisponible()) {
            throw new ProductoAgotadoException(producto.nombre());
        }
        LineaCarrito existente = lineas.get(producto.nombre());
        int cantidadActual = existente == null ? 0 : existente.cantidad();
        int nuevaCantidad = ajustarCantidad(cantidadActual + ajustarCantidad(cantidad));
        lineas.put(producto.nombre(), new LineaCarrito(producto, nuevaCantidad));
    }

    /**
     * Reemplaza la cantidad de un producto que ya esta en el carrito (HU02-CA03).
     *
     * @throws ProductoNoEnCarritoException si el producto no esta en el carrito
     */
    public void cambiarCantidad(String nombreProducto, int cantidad) {
        LineaCarrito existente = lineas.get(nombreProducto);
        if (existente == null) {
            throw new ProductoNoEnCarritoException(nombreProducto);
        }
        lineas.put(nombreProducto, new LineaCarrito(existente.producto(), ajustarCantidad(cantidad)));
    }

    /** Lineas del carrito, en una lista que no se puede modificar. */
    public List<LineaCarrito> lineas() {
        return List.copyOf(lineas.values());
    }

    /** Numero total de unidades en el carrito. */
    public int cantidadTotal() {
        return lineas.values().stream().mapToInt(LineaCarrito::cantidad).sum();
    }

    public boolean estaVacio() {
        return lineas.isEmpty();
    }

    /** Suma de los subtotales de todas las lineas, antes de descuentos (HU02-CA02). */
    public BigDecimal subtotal() {
        return lineas.values().stream()
                .map(LineaCarrito::subtotal)
                .reduce(CERO, BigDecimal::add);
    }

    /** Total a pagar. */
    public BigDecimal total() {
        return subtotal();
    }

    private static int ajustarCantidad(int cantidad) {
        return Math.clamp(cantidad, CANTIDAD_MINIMA, CANTIDAD_MAXIMA);
    }
}
