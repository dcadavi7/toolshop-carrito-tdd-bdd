package com.toolshop.carrito.bdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.toolshop.carrito.Carrito;
import com.toolshop.carrito.LineaCarrito;
import com.toolshop.carrito.Producto;
import com.toolshop.carrito.ProductoAgotadoException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Definiciones de pasos de features/carrito.feature.
 * Cucumber crea una instancia nueva por escenario, asi que el estado no se comparte entre escenarios.
 */
public class CarritoSteps {

    private final Map<String, Producto> catalogo = new HashMap<>();
    private Carrito carrito;
    private Runnable operacionPendiente;

    @Given("el catalogo tiene los productos:")
    public void elCatalogoTieneLosProductos(DataTable tabla) {
        for (Map<String, String> fila : tabla.asMaps()) {
            Producto producto = new Producto(
                    fila.get("nombre"),
                    new BigDecimal(fila.get("precio")),
                    Integer.parseInt(fila.get("stock")),
                    fila.get("co2"));
            catalogo.put(producto.nombre(), producto);
        }
    }

    @Given("tengo un carrito vacio")
    public void tengoUnCarritoVacio() {
        carrito = new Carrito();
    }

    @When("agrego {int} unidades de {string}")
    public void agregoUnidadesDe(int cantidad, String nombre) {
        carrito.agregar(producto(nombre), cantidad);
    }

    @When("intento agregar {int} unidades de {string}")
    public void intentoAgregarUnidadesDe(int cantidad, String nombre) {
        operacionPendiente = () -> carrito.agregar(producto(nombre), cantidad);
    }

    @When("cambio la cantidad de {string} a {int}")
    public void cambioLaCantidadDeA(String nombre, int cantidad) {
        carrito.cambiarCantidad(nombre, cantidad);
    }

    @Then("el carrito tiene {int} unidades en total")
    public void elCarritoTieneUnidadesEnTotal(int esperadas) {
        assertThat(carrito.cantidadTotal()).isEqualTo(esperadas);
    }

    @Then("la linea de {string} tiene {int} unidades")
    public void laLineaDeTieneUnidades(String nombre, int esperadas) {
        assertThat(linea(nombre).cantidad()).isEqualTo(esperadas);
    }

    @Then("el subtotal de {string} es {bigdecimal}")
    public void elSubtotalDeEs(String nombre, BigDecimal esperado) {
        assertThat(linea(nombre).subtotal()).isEqualByComparingTo(esperado);
    }

    @Then("el total del carrito es {bigdecimal}")
    public void elTotalDelCarritoEs(BigDecimal esperado) {
        assertThat(carrito.total()).isEqualByComparingTo(esperado);
    }

    @Then("el descuento ecologico es {bigdecimal}")
    public void elDescuentoEcologicoEs(BigDecimal esperado) {
        assertThat(carrito.descuentoEcologico()).isEqualByComparingTo(esperado);
    }

    @Then("el sistema rechaza el producto porque esta agotado")
    public void elSistemaRechazaElProductoPorqueEstaAgotado() {
        assertThatThrownBy(operacionPendiente::run).isInstanceOf(ProductoAgotadoException.class);
    }

    @Then("el carrito esta vacio")
    public void elCarritoEstaVacio() {
        assertThat(carrito.estaVacio()).isTrue();
    }

    private Producto producto(String nombre) {
        Producto producto = catalogo.get(nombre);
        assertThat(producto).as("El producto %s no esta en el catalogo", nombre).isNotNull();
        return producto;
    }

    private LineaCarrito linea(String nombre) {
        return carrito.lineas().stream()
                .filter(l -> l.producto().nombre().equals(nombre))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No hay una linea para " + nombre));
    }
}
