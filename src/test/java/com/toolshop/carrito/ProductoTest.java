package com.toolshop.carrito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Producto")
class ProductoTest {

    @Test
    @DisplayName("un producto con stock esta disponible y sin stock no")
    void disponibilidad() {
        assertThat(new Producto("Hammer", new BigDecimal("12.58"), 3, "B").estaDisponible()).isTrue();
        assertThat(new Producto("Hammer", new BigDecimal("12.58"), 0, "B").estaDisponible()).isFalse();
    }

    @ParameterizedTest(name = "calificacion CO2 {0} -> ecologico: {1}")
    @CsvSource({"A, true", "B, true", "a, true", "C, false", "D, false", "E, false"})
    @DisplayName("solo las calificaciones CO2 A y B son ecologicas")
    void esEcologico(String co2, boolean esperado) {
        assertThat(new Producto("Pliers", new BigDecimal("12.01"), 5, co2).esEcologico()).isEqualTo(esperado);
    }

    @Test
    @DisplayName("sin calificacion CO2 el producto no es ecologico")
    void sinCalificacion() {
        assertThat(new Producto("Pliers", new BigDecimal("12.01"), 5, null).esEcologico()).isFalse();
    }

    @Test
    @DisplayName("no se permiten precios negativos")
    void precioNegativo() {
        BigDecimal precioNegativo = new BigDecimal("-1");
        assertThatThrownBy(() -> new Producto("Pliers", precioNegativo, 5, "A"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("no se permite stock negativo")
    void stockNegativo() {
        assertThatThrownBy(() -> new Producto("Pliers", BigDecimal.ONE, -1, "A"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("el nombre y el precio son obligatorios")
    void obligatorios() {
        assertThatThrownBy(() -> new Producto(null, BigDecimal.ONE, 1, "A")).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Producto("Pliers", null, 1, "A")).isInstanceOf(NullPointerException.class);
    }
}
