# Evidencias de funcionamiento

Completar esta lista durante la prueba con MySQL. No editar manualmente los
archivos JSON durante la demostracion.

- [ ] `scripts\probar.cmd` termina con `PRUEBAS_OK`.
- [ ] Conexion exitosa a `sistema_empresa`.
- [ ] Importacion de `productos.json` muestra la cantidad importada.
- [ ] Registro de proveedor visible en MySQL.
- [ ] RFC repetido rechazado.
- [ ] Compra con fecha invalida rechazada.
- [ ] Compra registrada en estado `PENDIENTE`.
- [ ] Cantidad cero o costo cero rechazados.
- [ ] Detalle guardado con subtotal correcto.
- [ ] Total de compra coincide con la suma de subtotales.
- [ ] Compra confirmada.
- [ ] No se agregan detalles despues de confirmar.
- [ ] `entradas_inventario.json` se genera sin edicion manual.
- [ ] JSON contiene la fecha de compra y `tipo: "ENTRADA"`.
- [ ] Equipo 1 recibe e importa el archivo durante la integracion.

Capturas sugeridas:

1. Submenu completo.
2. Validacion fallida.
3. Registro exitoso.
4. Consultas de `database/verificacion_compras.sql`.
5. Contenido del JSON exportado.
6. Resultado de importacion del Equipo 1.
