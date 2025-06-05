
# ProyectoFullStack1
Aqui se guardara el avance del proyecto de fullstack 1 2025-1
Repositorio monolitico que guardara todos los microservicios<br><br>
# Comandos (terminal control + ñ)<br><br>
git config --global user.email "EMAIL"<br>
git config --global user.name "USERNAME"<br><br>

# Actualizar Archivos<br>
git pull<br><br>
# Microservicios - formato de datos<br><br>
JSON<br>

# Cuentas<br>
Usuario<br>
{
    "email" : String,
    "nombreUsuario" : String,
    "password" : String,
    "tipoUsuario" : String
}<br>
# Administracion<br>
NO INGRESAMOS ALERTAS NOSOTROS A MANO, LAS GENERA LOS SISTEMA SOLO DEBEMOS HACER LOS GET<br>
Alerta<br>
{
    "mensaje" : String,
    "tipoAlerta" : String,
}<br>
# Producto<br>
Producto<br>
{
    "nombre" : String,
    "marca" : String,
    "precio" : int,
}<br>
# Reseña<br>
Reseña<br>
{
    "productoId" : int,
    "usuarioId" : int,
    "comentario" : String
}<br>
# Carrito<br>
Carrito<br>
{
    "usuarioId" : int,
    "listaProductos" : [
        ProductoCarrito
    ]
}<br>
ProductoCarrito<br>
{
  "productoId": int,
  "nombre": String,
  "marca": String,
  "cantidad": int
}<br>
# Venta<br>
Venta<br>
{
    "usuarioId" : int,
    "vendedorId" : int,
    "carritoId" : int
}
# Inventario<br>
EL INVENTARIO SE CREA AUTOMATICAMENTE CUANDO CREAMOS LA SUCURSAL<BR>
Inventario<br>
{

}<br>
ProductoInventario<br>
{
  "productoId": int,
  "cantidad": int
}<br>