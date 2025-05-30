package com.patitofeliz.sale_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sale_service.model.Carrito;
import com.patitofeliz.sale_service.model.CarritoProducto;
import com.patitofeliz.sale_service.model.conexion.Alerta;
import com.patitofeliz.sale_service.model.conexion.Producto;
import com.patitofeliz.sale_service.model.conexion.Usuario;
import com.patitofeliz.sale_service.repository.ReporitoryCarrito;

@Service
public class CarritoService 
{
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ReporitoryCarrito carritoRepository;

    private static final String PRODUCTO_API = "http://localhost:8003/producto";
    private static final String USUARIO_API = "http://localhost:8001/usuario";
    private static final String ALERTA_API = "http://localhost:8002/alerta";

    public List<Carrito> getCarritos()
    {
        return carritoRepository.findAll();
    }

    public List<Carrito> getCarritosByUsuarioid(int id)
    {
        return carritoRepository.findByUsuarioId(id);
    }

    public Carrito getCarrito(int id)
    {
        return carritoRepository.findById(id).orElse(null);
    }

    public Carrito guardar(Carrito carrito)
    {
        carrito.setListaProductos(normalizarCarrito(carrito.getListaProductos()));

        Integer total = calcularTotal(carrito);
        carrito.setTotal(total);

        Usuario usuario = obtenerUsuario(carrito.getUsuarioId());

        Carrito nuevo = carritoRepository.save(carrito);

        crearAlerta("Carrito registrado - Dueño: "+usuario.getNombreUsuario(), "Aviso: Carrito");

        return nuevo;
    }

    public Carrito actualizar(int id, Carrito carritoActualizado)
    {
        Carrito carritoActual = carritoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));

        carritoActual.setListaProductos(normalizarCarrito(carritoActualizado.getListaProductos()));

        return carritoRepository.save(carritoActual);
    }

    public void borrar(int id)
    {
        carritoRepository.deleteById(id);
    }

    private Integer calcularTotal(Carrito carrito) 
    {
        int total = 0;

        if (carrito.getListaProductos() == null || carrito.getListaProductos().isEmpty())
            return total;

        for (CarritoProducto producto : carrito.getListaProductos()) 
        {
            Producto productoExtraido = obtenerProducto(producto.getProductoId());

            if (productoExtraido != null)
            {
                total += productoExtraido.getPrecio() * producto.getCantidad();

                producto.setNombre(productoExtraido.getNombre());
                producto.setMarca(productoExtraido.getMarca());
            }

        }
        return total;
    }

    // AUXILIARES

    // Metodo que verificara si hay dos veces ingresado un producto en el carrito,
    // si es el caso, el metodo fusionara las cantidades en el carrito y dejara solo 1 copia
    // Ejemplo Carrito: auto-400, bici-200, auto-600 -|Entra al metodo|-> auto-1000, bici-600
    private List<CarritoProducto> normalizarCarrito(List<CarritoProducto> productosCarrito)
    {
        List<CarritoProducto> productos = productosCarrito;

        return productos;
    }

    private Usuario obtenerUsuario(int usuarioId) 
    {
        Usuario usuario = restTemplate.getForObject(USUARIO_API + "/" + usuarioId, Usuario.class);

        if (usuario == null)
            throw new NoSuchElementException("Usuario no encontrado con ID: " + usuarioId);

        return usuario;
    }

    private Producto obtenerProducto(int productoId) 
    {
        Producto producto = restTemplate.getForObject(PRODUCTO_API + "/" + productoId, Producto.class);

        if (producto == null)
            throw new NoSuchElementException("Producto no encontrado con ID: " + productoId);

        return producto;
    }

    private void crearAlerta(String mensaje, String tipoAlerta)
    {
        Alerta alertaProductoRegistrado = new Alerta(mensaje, tipoAlerta);

        try
        {
            restTemplate.postForObject(ALERTA_API, alertaProductoRegistrado, Alerta.class);
        }
        catch (RestClientException e)
        {
            throw new IllegalArgumentException("No se pudo ingresar la Alerta: "+e);
        }
    }

}
