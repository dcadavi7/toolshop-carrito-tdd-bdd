# Toolshop — Carrito (TDD + BDD + CI/CD)

Cálculo del carrito de compras de [Toolshop](https://practicesoftwaretesting.com) desarrollado con **TDD** (JUnit 5)
y validado con **BDD** (Cucumber), con integración continua en **GitHub Actions**, cobertura con **JaCoCo** y
análisis de calidad en **SonarCloud**.

## Reglas de negocio (HU-02)

| Criterio | Regla |
|---|---|
| HU02-CA01 | Agregar un producto disponible; si ya estaba, se acumulan las unidades |
| HU02-CA02 | Subtotal = precio × cantidad; total = suma de líneas (2 decimales) |
| HU02-CA03 | Cambiar la cantidad recalcula subtotal y total |
| HU02-CA04 | Un producto agotado no se puede agregar |
| HU02-CA05 | 5 % de descuento si más del 50 % de las unidades son ecológicas (CO2 A o B) |
| HU02-CA06 | La cantidad se ajusta al rango de 1 a 99 |

Cada criterio tiene pruebas unitarias en `CarritoTest` (`@Tag("HU02-CAxx")`) y escenarios en
`src/test/resources/features/carrito.feature` (`@HU02_CAxx`).

## Ejecutar localmente (Java 21 + Maven)

```
mvn -P tdd test          # solo pruebas unitarias
mvn -P bdd test          # solo escenarios BDD
mvn verify               # todo, con reporte y umbral de cobertura de JaCoCo
```

Reportes: `target/site/jacoco/index.html` (cobertura) y `target/cucumber/reporte.html` (BDD).

## Pipeline

`.github/workflows/ci.yml` ejecuta en cada push a `main` y en cada pull request:
pruebas TDD → pruebas BDD → cobertura JaCoCo (mínimo 90 % de líneas y 80 % de ramas) → análisis y quality gate de SonarCloud → publicación de reportes.
