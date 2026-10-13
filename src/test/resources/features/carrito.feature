# Historia HU-02 (actividad 2), ampliada con las reglas HU02-CA05 y HU02-CA06 encontradas en Toolshop.
Feature: HU-02 Carrito de compras
  Como cliente de la ferreteria,
  quiero agregar herramientas al carrito y ajustar las cantidades
  para saber cuanto voy a pagar antes de finalizar la compra.

  Background:
    Given el catalogo tiene los productos:
      | nombre             | precio | stock | co2 |
      | Adjustable Wrench  | 20.33  | 25    | C   |
      | Tape Measure 5m    | 12.91  | 25    | C   |
      | Long Nose Pliers   | 14.24  | 0     | C   |
      | Combination Pliers | 14.15  | 25    | A   |
    And tengo un carrito vacio

  @HU02_CA01
  Scenario: Agregar un producto disponible al carrito
    When agrego 2 unidades de "Adjustable Wrench"
    Then el carrito tiene 2 unidades en total

  @HU02_CA01
  Scenario: Agregar dos veces el mismo producto acumula las unidades
    When agrego 2 unidades de "Adjustable Wrench"
    And agrego 3 unidades de "Adjustable Wrench"
    Then la linea de "Adjustable Wrench" tiene 5 unidades

  @HU02_CA02
  Scenario Outline: El carrito calcula el subtotal y el total
    When agrego <cantidad> unidades de "<producto>"
    Then el subtotal de "<producto>" es <subtotal>
    And el total del carrito es <subtotal>

    Examples:
      | producto          | cantidad | subtotal |
      | Adjustable Wrench | 2        | 40.66    |
      | Tape Measure 5m   | 3        | 38.73    |

  @HU02_CA02
  Scenario: El total suma todas las lineas del carrito
    When agrego 2 unidades de "Adjustable Wrench"
    And agrego 3 unidades de "Tape Measure 5m"
    Then el total del carrito es 79.39

  @HU02_CA03
  Scenario: Cambiar la cantidad recalcula el total
    Given agrego 1 unidades de "Adjustable Wrench"
    When cambio la cantidad de "Adjustable Wrench" a 3
    Then el subtotal de "Adjustable Wrench" es 60.99
    And el total del carrito es 60.99

  @HU02_CA04
  Scenario: Un producto agotado no se puede agregar al carrito
    When intento agregar 1 unidades de "Long Nose Pliers"
    Then el sistema rechaza el producto porque esta agotado
    And el carrito esta vacio

  @HU02_CA05
  Scenario Outline: Descuento ecologico cuando mas de la mitad de las unidades son ecologicas
    Given agrego <ecologicas> unidades de "Combination Pliers"
    And agrego <no_ecologicas> unidades de "Adjustable Wrench"
    Then el descuento ecologico es <descuento>
    And el total del carrito es <total>

    Examples:
      | ecologicas | no_ecologicas | descuento | total |
      | 1          | 1             | 0.00      | 34.48 |
      | 2          | 1             | 2.43      | 46.20 |
      | 3          | 2             | 4.16      | 78.95 |

  @HU02_CA06
  Scenario Outline: Las cantidades fuera de rango se ajustan entre 1 y 99
    When agrego <pedidas> unidades de "Tape Measure 5m"
    Then la linea de "Tape Measure 5m" tiene <en_carrito> unidades

    Examples:
      | pedidas | en_carrito |
      | 0       | 1          |
      | 1       | 1          |
      | 99      | 99         |
      | 100     | 99         |
