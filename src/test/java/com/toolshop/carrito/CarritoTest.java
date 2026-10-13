package com.toolshop.carrito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Pruebas unitarias (TDD) del carrito de compras.
 * Cada clase anidada corresponde a un criterio de aceptacion de la HU-02.
 */
@DisplayName("Carrito de compras")
class CarritoTest {

    private static final Producto LLAVE_AJUSTABLE = new Producto("Adjustable Wrench", new BigDecimal("20.33"), 25, "C");
    private static final Producto CINTA_METRICA = new Producto("Tape Measure 5m", new BigDecimal("12.91"), 25, "C");
    private static final Producto ALICATE_AGOTADO = new Producto("Long Nose Pliers", new BigDecimal("14.24"), 0, "C");
    private static final Producto ALICATE_ECOLOGICO = new Producto("Combination Pliers", new BigDecimal("14.15"), 25, "A");

    private Carrito carrito;

    @BeforeEach
    void crearCarritoVacio() {
        carrito = new Carrito();
    }

    @Nested
    @Tag("HU02-CA01")
    @DisplayName("HU02-CA01 Agregar un producto disponible")
    class AgregarProducto {

        @Test
        @DisplayName("un carrito nuevo esta vacio")
        void carritoNuevoVacio() {
            assertThat(carrito.estaVacio()).isTrue();
            assertThat(carrito.cantidadTotal()).isZero();
        }

        @Test
        @DisplayName("agregar un producto crea una linea con la cantidad elegida")
        void agregarProductoNuevo() {
            carrito.agregar(LLAVE_AJUSTABLE, 2);

            assertThat(carrito.lineas()).hasSize(1);
            assertThat(carrito.lineas().get(0).producto()).isEqualTo(LLAVE_AJUSTABLE);
            assertThat(carrito.cantidadTotal()).isEqualTo(2);
        }

        @Test
        @DisplayName("agregar el mismo producto dos veces acumula las unidades en una sola linea")
        void agregarMismoProductoAcumula() {
            carrito.agregar(LLAVE_AJUSTABLE, 2);
            carrito.agregar(LLAVE_AJUSTABLE, 3);

            assertThat(carrito.lineas()).hasSize(1);
            assertThat(carrito.lineas().get(0).cantidad()).isEqualTo(5);
        }

        @Test
        @DisplayName("la lista de lineas expuesta no se puede modificar desde afuera")
        void lineasNoModificables() {
            carrito.agregar(LLAVE_AJUSTABLE, 1);

            assertThatThrownBy(() -> carrito.lineas().clear())
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
