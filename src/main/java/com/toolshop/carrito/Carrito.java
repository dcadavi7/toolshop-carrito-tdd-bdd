package com.toolshop.carrito;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Carrito de compras de Toolshop (HU-02).
 * Las lineas se guardan por nombre de producto y conservan el orden en que se agregaron.
 */
public class Carrito {

    private final Map<String, LineaCarrito> lineas = new LinkedHashMap<>();

    /**
     * Agrega unidades de un producto; si ya estaba en el carrito, se acumulan (HU02-CA01).
     */
    public void agregar(Producto producto, int cantidad) {
        Objects.requireNonNull(producto, "El producto es obligatorio");
        LineaCarrito existente = lineas.get(producto.nombre());
        int cantidadActual = existente == null ? 0 : existente.cantidad();
        lineas.put(producto.nombre(), new LineaCarrito(producto, cantidadActual + cantidad));
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
}
