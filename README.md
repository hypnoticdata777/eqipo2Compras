# Equipo 2 — Compras y Proveedores

Modulo completo: Proveedor, Compra, DetalleCompra (Model / Repository / Service / Controller),
mas el submenu y el exportador de JSON. Compilado y verificado sin errores con `javac` (JDK 21).

## Como esta organizado

```
src/com/empresa/
├── Conexion.java                  <- SOLO REFERENCIA, lee la nota abajo
├── compras/
│   ├── ComprasMenu.java           <- submenu (capa "App/menu")
│   ├── MainPruebaCompras.java     <- arnes para probar este modulo solo
│   ├── proveedor/                 <- Model, Repository, Service, Controller
│   ├── compra/                    <- Model, Repository, Service, Controller
│   └── detallecompra/             <- Model, Repository, Service, Controller
└── json/
    ├── dto/EntradaInventarioDTO.java
    └── exportador/ExportadorEntradasInventario.java

database/schema_pruebas_compras.sql   <- para probar en tu MySQL local
exportaciones/                        <- aqui se escribe entradas_inventario.json
```

## ⚠️ Sobre Conexion.java

Si el repo compartido de Live Share ya tiene un `Conexion.java`, **usa ese, no este**.
Este archivo es solo para que el paquete compile y corra de forma aislada. Si el de tu
equipo tiene un metodo distinto a `Conexion.getConexion()`, ajusta los imports en los
3 Repository (`ProveedorRepository`, `CompraRepository`, `DetalleCompraRepository`).

## Como probarlo SOLO (antes de integrar)

1. Corre `database/schema_pruebas_compras.sql` en tu MySQL local (crea la BD
   `sistema_empresa` con las 3 tablas reales + una tabla `producto` de prueba).
2. Agrega el conector de MySQL (`mysql-connector-j.jar`) a tu classpath/librerias en
   VS Code o tu IDE.
3. Corre `MainPruebaCompras.main()`. Te abre el menu de Compras directo, sin pasar
   por el menu principal del proyecto.

## Como integrarlo al proyecto compartido

Cuando te toque el turno en `App.java`, en el switch del menu principal agrega:

```java
case 2 -> new ComprasMenu().mostrarMenu();
```

y arriba del archivo: `import com.empresa.compras.ComprasMenu;`

No se modifico ningun otro archivo del proyecto compartido.

## Decisiones de diseño (para defender en la explicacion oral)

**1. "Proveedor existente" vs "activo"** — El documento dice literal "Proveedor
existente" para Compra (no dice "y activo", a diferencia de Envios donde si dice
explicito "Transportista activo"). Por eso `ProveedorRepository.existe()` solo
checa que el ID exista, sin importar si esta activo. Mismo criterio para
"Producto existente" en DetalleCompra.

**2. Validacion de producto sin importar clases del Equipo 1** — `DetalleCompraRepository`
valida que el producto exista con una consulta SQL directa a la tabla compartida
`producto`, en vez de importar `Producto`/`ProductoRepository` del Equipo 1. Como
las 4 equipos comparten la misma base `sistema_empresa`, el Repository puede
consultar esa tabla sin depender del codigo Java de otro equipo. Esto evita
errores si el Equipo 1 todavia no termina su parte, y mantiene a Compras 100%
independiente para poder probarlo solo.

**3. JSON escrito a mano, sin Gson** — `ExportadorEntradasInventario` construye el
JSON con StringBuilder en vez de usar la libreria Gson. Si el equipo ya tiene
`lib/gson.jar` agregado al proyecto, se puede cambiar facil por `gson.toJson(lista)`
sin tocar el DTO ni el contrato.

**4. `idMovimiento` se exporta como 0** — El campo `idMovimiento` del contrato
`entradas_inventario.json` se manda como 0 porque ese ID lo genera MySQL
(autoincremento de `movimiento_inventario`) hasta que el Equipo 1 importe el
archivo e inserte las filas. Compras no inserta en esa tabla, solo exporta.

**5. Exportar solo permite compras CONFIRMADAS** — Una compra PENDIENTE todavia
puede cambiar (se le siguen agregando productos), y una CANCELADA nunca debio
generar movimiento de inventario. Por eso el export se bloquea si el estado no
es CONFIRMADA.

## Algo que falta confirmar con el grupo / profesor

El documento marca a Equipo 2 como **consumidor** de `productos.json` (lo produce
Equipo 1), pero ningun modulo de Compras tiene listada una funcion explicita de
"Importar productos.json". En este modulo, la validacion de producto se hace por
SQL directo a la tabla compartida (ver punto 2 arriba), no por un importador de
JSON. Si el profesor pide ver un importador formal para que cuente como "consumidor"
del contrato, es facil agregar un `ImportadorProductos.java` — avisa si lo necesitas.

## Limitacion de esta entrega

Todo el codigo compila limpio (0 errores, 0 warnings, javac 21) pero **no se probo
contra una MySQL real corriendo**, porque este entorno de desarrollo no tiene
acceso a un servidor MySQL ni al .jar del conector (sin acceso a Maven Central).
Antes de la presentacion, corre el script de prueba y ejecuta `MainPruebaCompras`
contra tu MySQL local, o directo contra la base compartida del profesor, para
confirmar el comportamiento en tiempo de ejecucion.
