
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
Alerta<br>
{
    "mensaje" : String,
    "tipoAlerta" : String,
    "fecha" : String
}<br>
# Inventario<br>
Producto<br>
{
    "nombre" : String,
    "marca" : String,
    "precio" : int,
    "cantidadInventario" : int
}<br>
# Reseña<br>
Reseña<br>
{
    "productoId" : int,
    "usuarioId" : int,
    "autor" : String,
    "comentario" : String
}<br>
# Venta<br>
Carrito<br>
{
    "usuarioId" : int,
    "listaProductos" : [
        {
        "productoId" : int,
        "cantidad" : int
        }
        ],
    "total" : int
}<br>
Venta<br>
{
    "usuarioId" : int,
    "vendedorId" : int,
    "carritoId" : int
}