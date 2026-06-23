-- Esquema MINIMO para probar el paquete "compras" de forma INDEPENDIENTE
-- en tu propia MySQL local, antes de conectarte a la base compartida
-- "sistema_empresa" que crea el profesor.
--
-- Nombres de tabla, columnas y tipos respetan EXACTAMENTE lo que pide
-- el documento del proyecto. No los cambies al integrar con el equipo.

CREATE DATABASE IF NOT EXISTS sistema_empresa;
USE sistema_empresa;

CREATE TABLE IF NOT EXISTS proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(150) NOT NULL,
    rfc          VARCHAR(20)  NOT NULL UNIQUE,
    telefono     VARCHAR(20)  NOT NULL,
    correo       VARCHAR(150) NOT NULL,
    direccion    VARCHAR(255),
    activo       BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS compra (
    id_compra    INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    fecha        VARCHAR(20) NOT NULL,
    total        DOUBLE NOT NULL DEFAULT 0,
    estado       VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor)
);

CREATE TABLE IF NOT EXISTS detalle_compra (
    id_detalle_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_compra         INT NOT NULL,
    id_producto       INT NOT NULL,
    cantidad          INT NOT NULL,
    costo_unitario    DOUBLE NOT NULL,
    subtotal          DOUBLE NOT NULL,
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra)
);

-- SOLO para poder probar tu paquete de forma aislada SIN depender del Equipo 1.
-- Bórrala / ignórala cuando te conectes a la base real "sistema_empresa" del
-- profesor, ya que ahí la tabla producto la crea y llena el Equipo 1 (Inventario).
CREATE TABLE IF NOT EXISTS producto (
    id_producto  INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(150) NOT NULL,
    descripcion  VARCHAR(255),
    precio       DOUBLE NOT NULL,
    stock        INT NOT NULL DEFAULT 0,
    id_categoria INT,
    id_almacen   INT,
    activo       BOOLEAN NOT NULL DEFAULT TRUE
);

-- Dato idempotente para poder ejecutar el script varias veces sin duplicados.
INSERT INTO producto
    (id_producto, nombre, descripcion, precio, stock, id_categoria, id_almacen, activo)
VALUES
    (1, 'Mouse inalambrico', 'Producto de prueba', 250.00, 10, 1, 1, TRUE)
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    descripcion = VALUES(descripcion),
    precio = VALUES(precio),
    stock = VALUES(stock),
    id_categoria = VALUES(id_categoria),
    id_almacen = VALUES(id_almacen),
    activo = VALUES(activo);
