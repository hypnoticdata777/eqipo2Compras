USE sistema_empresa;

SELECT id_proveedor, nombre, rfc, telefono, correo, direccion, activo
FROM proveedor
ORDER BY id_proveedor;

SELECT id_compra, id_proveedor, fecha, total, estado
FROM compra
ORDER BY id_compra;

SELECT id_detalle_compra, id_compra, id_producto, cantidad, costo_unitario, subtotal
FROM detalle_compra
ORDER BY id_compra, id_detalle_compra;

SELECT
    c.id_compra,
    c.total AS total_guardado,
    COALESCE(SUM(d.subtotal), 0) AS total_calculado,
    c.total = COALESCE(SUM(d.subtotal), 0) AS total_correcto
FROM compra c
LEFT JOIN detalle_compra d ON d.id_compra = c.id_compra
GROUP BY c.id_compra, c.total
ORDER BY c.id_compra;
