# Equipo 2 - Compras y proveedores

Implementacion independiente de los modulos `proveedor`, `compra` y
`detallecompra` para el proyecto final Sistema Empresa.

## Cumplimiento principal

- Arquitectura: Menu -> Controller -> Service -> Repository -> MySQL.
- Model, Repository, Service y Controller para los tres modulos.
- Registro, consulta, busqueda, desactivacion y cambios de estado.
- Subtotal calculado en Service y total actualizado en una transaccion.
- Productor de `exportaciones/entradas_inventario.json`.
- Consumidor de `importaciones/productos.json`.
- Errores de MySQL propagados sin mostrar exitos falsos ni cerrar el menu.
- Pruebas automaticas de validaciones, parser, importador y exportador JSON.

## Estructura

```text
.vscode/                  Configuracion para Java en VS Code
database/                 Esquema local y consultas de verificacion
exportaciones/            Salida de entradas_inventario.json
importaciones/            Entrada de productos.json y archivo de ejemplo
lib/                      MySQL Connector/J y Gson
scripts/                  Compilar, ejecutar y probar desde CMD
src/com/empresa/
  compras/
    proveedor/
    compra/
    detallecompra/
    ComprasMenu.java
    MainPruebaCompras.java
  json/
    dto/
    importador/
    exportador/
    util/
test/                     Pruebas que no requieren MySQL
```

## Requisitos

- JDK 21 o posterior.
- MySQL 8 o posterior para la prueba integral.
- Base de datos `sistema_empresa`.

El repositorio incluye:

- `lib/mysql-connector-j.jar`: MySQL Connector/J 9.7.0.
- `lib/gson.jar`: Gson 2.14.0, incluido para respetar la estructura compartida.

El codigo JSON actual no depende de Gson, por lo que los contratos tambien
pueden probarse con `javac` y la biblioteca estandar.

## Pruebas rapidas sin MySQL

Desde CMD, dentro de la raiz del repositorio:

```cmd
scripts\probar.cmd
```

Resultado esperado:

```text
PRUEBAS_OK: contratos JSON, calculos y validaciones locales.
```

Estas pruebas verifican:

- validaciones locales antes de consultar MySQL;
- calculo de subtotal;
- lectura del contrato `productos.json`;
- generacion y lectura de `entradas_inventario.json`;
- fecha de compra y tipo `ENTRADA`.

## Preparar MySQL local

Ejecuta:

```text
database/schema_pruebas_compras.sql
```

El script es idempotente: puede ejecutarse varias veces sin duplicar el
producto de prueba.

La conexion predeterminada es:

```text
jdbc:mysql://localhost:3306/sistema_empresa
usuario: root
password: vacio
```

Puedes cambiarla sin editar Java:

```cmd
set DB_URL=jdbc:mysql://localhost:3306/sistema_empresa
set DB_USER=root
set DB_PASSWORD=tu_password
scripts\ejecutar.cmd
```

Tambien se aceptan propiedades de JVM: `db.url`, `db.user` y `db.password`.

## Ejecutar el submenu

```cmd
scripts\ejecutar.cmd
```

Flujo recomendado para la demostracion:

1. Importar `importaciones/productos_ejemplo.json`.
2. Registrar un proveedor.
3. Registrar una compra con fecha `YYYY-MM-DD`.
4. Agregar el producto importado a la compra.
5. Mostrar detalles y comprobar el total.
6. Confirmar la compra.
7. Exportar `exportaciones/entradas_inventario.json`.
8. Abrir el JSON sin editarlo y entregarlo al Equipo 1.
9. Ejecutar `database/verificacion_compras.sql`.

## Contratos JSON

### productos.json

Equipo 2 consume los campos exactos:

`idProducto`, `nombre`, `descripcion`, `precio`, `stock`, `idCategoria`,
`nombreCategoria`, `idAlmacen`, `nombreAlmacen`, `activo`.

El importador valida estructura, tipos, IDs, precio, stock y duplicados. Los
productos validos se insertan o actualizan dentro de una transaccion.

En la base compartida, las categorias y almacenes referenciados deben existir.

### entradas_inventario.json

Equipo 2 produce:

`idMovimiento`, `idCompra`, `idProducto`, `cantidad`, `costoUnitario`,
`fecha`, `tipo`.

`tipo` siempre vale `ENTRADA`. `idMovimiento` se entrega como `0` porque el
Equipo 1 genera el ID real al registrar el movimiento en MySQL.

## Integracion en App.java

Cuando el responsable del menu general integre el modulo:

```java
import com.empresa.compras.ComprasMenu;

// Dentro del switch principal:
case 2 -> new ComprasMenu().mostrarMenu();
```

Debe conservarse el `Conexion.java` oficial del proyecto compartido. Si su
firma no es `Conexion.getConexion()`, se ajustan solamente las llamadas de los
Repository.

## Evidencia para la entrega

Usa [EVIDENCIAS.md](EVIDENCIAS.md) como lista de capturas y resultados que deben
guardarse antes de la presentacion.
