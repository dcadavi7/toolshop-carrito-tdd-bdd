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

    @Nested
    @Tag("HU02-CA02")
    @DisplayName("HU02-CA02 Subtotal por linea y total del carrito")
    class SubtotalYTotal {

        @Test
        @DisplayName("el subtotal de una linea es precio por cantidad")
        void subtotalDeLinea() {
            carrito.agregar(LLAVE_AJUSTABLE, 2);

            assertThat(carrito.lineas().get(0).subtotal()).isEqualByComparingTo("40.66");
        }

        @Test
        @DisplayName("el total suma los subtotales de todas las lineas")
        void totalDeVariasLineas() {
            carrito.agregar(LLAVE_AJUSTABLE, 2);
            carrito.agregar(CINTA_METRICA, 3);

            assertThat(carrito.subtotal()).isEqualByComparingTo("79.39");
            assertThat(carrito.total()).isEqualByComparingTo("79.39");
        }

        @Test
        @DisplayName("el total de un carrito vacio es cero con dos decimales")
        void totalCarritoVacio() {
            assertThat(carrito.total()).isEqualTo(new BigDecimal("0.00"));
        }
    }

    @Nested
    @Tag("HU02-CA03")
    @DisplayName("HU02-CA03 Cambiar la cantidad recalcula el total")
    class CambiarCantidad {

        @Test
        @DisplayName("cambiar la cantidad actualiza el subtotal y el total")
        void cambiarCantidadRecalcula() {
            carrito.agregar(LLAVE_AJUSTABLE, 1);

            carrito.cambiarCantidad("Adjustable Wrench", 3);

            assertThat(carrito.lineas().get(0).cantidad()).isEqualTo(3);
            assertThat(carrito.total()).isEqualByComparingTo("60.99");
        }

        @Test
        @DisplayName("cambiar la cantidad de un producto que no esta en el carrito es un error")
        void cambiarProductoInexistente() {
            assertThatThrownBy(() -> carrito.cambiarCantidad("Hammer", 2))
                    .isInstanceOf(ProductoNoEnCarritoException.class)
                    .hasMessageContaining("Hammer");
        }
    }

    @Nested
    @Tag("HU02-CA04")
    @DisplayName("HU02-CA04 Un producto agotado no se puede agregar")
    class ProductoAgotado {

        @Test
        @DisplayName("agregar un producto sin stock lanza ProductoAgotadoException")
        void agregarAgotadoLanzaExcepcion() {
            assertThatThrownBy(() -> carrito.agregar(ALICATE_AGOTADO, 1))
                    .isInstanceOf(ProductoAgotadoException.class)
                    .hasMessageContaining("Long Nose Pliers");
        }

        @Test
        @DisplayName("si se rechaza un producto agotado, el carrito queda igual")
        void carritoNoCambiaAlRechazar() {
            carrito.agregar(LLAVE_AJUSTABLE, 1);

            assertThatThrownBy(() -> carrito.agregar(ALICATE_AGOTADO, 1))
                    .isInstanceOf(ProductoAgotadoException.class);

            assertThat(carrito.lineas()).hasSize(1);
            assertThat(carrito.total()).isEqualByComparingTo("20.33");
        }
    }

    @Nested
    @Tag("HU02-CA06")
    @DisplayName("HU02-CA06 La cantidad se ajusta al rango de 1 a 99")
    class LimitesDeCantidad {

        @ParameterizedTest(name = "pedir {0} unidades deja {1} en el carrito")
        @CsvSource({"-5, 1", "0, 1", "1, 1", "98, 98", "99, 99", "100, 99"})
        @DisplayName("valores limite al agregar")
        void ajustaAlAgregar(int pedida, int esperada) {
            carrito.agregar(LLAVE_AJUSTABLE, pedida);

            assertThat(carrito.lineas().get(0).cantidad()).isEqualTo(esperada);
        }

        @Test
        @DisplayName("acumular unidades no supera el maximo de 99")
        void acumularNoSuperaMaximo() {
            carrito.agregar(LLAVE_AJUSTABLE, 60);
            carrito.agregar(LLAVE_AJUSTABLE, 60);

            assertThat(carrito.lineas().get(0).cantidad()).isEqualTo(Carrito.CANTIDAD_MAXIMA);
        }

        @ParameterizedTest(name = "cambiar a {0} unidades deja {1}")
        @CsvSource({"0, 1", "100, 99"})
        @DisplayName("valores limite al cambiar la cantidad")
        void ajustaAlCambiar(int pedida, int esperada) {
            carrito.agregar(LLAVE_AJUSTABLE, 5);

            carrito.cambiarCantidad("Adjustable Wrench", pedida);

            assertThat(carrito.lineas().get(0).cantidad()).isEqualTo(esperada);
        }
    }

    @Nested
    @Tag("HU02-CA05")
    @DisplayName("HU02-CA05 Descuento ecologico del 5 %")
    class DescuentoEcologico {

        @Test
        @DisplayName("sin productos no hay descuento")
        void carritoVacioSinDescuento() {
            assertThat(carrito.descuentoEcologico()).isEqualByComparingTo("0.00");
        }

        @Test
        @DisplayName("con exactamente el 50 % de unidades ecologicas no hay descuento")
        void cincuentaPorCientoSinDescuento() {
            carrito.agregar(ALICATE_ECOLOGICO, 1);
            carrito.agregar(LLAVE_AJUSTABLE, 1);

            assertThat(carrito.descuentoEcologico()).isEqualByComparingTo("0.00");
            assertThat(carrito.total()).isEqualByComparingTo("34.48");
        }

        @Test
        @DisplayName("con mas del 50 % de unidades ecologicas se descuenta el 5 % redondeado")
        void masDeLaMitadConDescuento() {
            carrito.agregar(ALICATE_ECOLOGICO, 2);
            carrito.agregar(LLAVE_AJUSTABLE, 1);

            assertThat(carrito.subtotal()).isEqualByComparingTo("48.63");
            assertThat(carrito.descuentoEcologico()).isEqualByComparingTo("2.43");
            assertThat(carrito.total()).isEqualByComparingTo("46.20");
        }

        @Test
        @DisplayName("solo productos ecologicos reciben el descuento")
        void soloEcologicos() {
            carrito.agregar(ALICATE_ECOLOGICO, 1);

            assertThat(carrito.total()).isEqualByComparingTo("13.44");
        }
    }
}
