package com.patitofeliz.carrito_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.carrito_service.model.Carrito;
import com.patitofeliz.carrito_service.model.CarritoProducto;
import com.patitofeliz.carrito_service.model.conexion.Alerta;
import com.patitofeliz.carrito_service.model.conexion.Producto;
import com.patitofeliz.carrito_service.model.conexion.Sucursal;
import com.patitofeliz.carrito_service.model.conexion.Usuario;
import com.patitofeliz.carrito_service.repository.RepositoryCarrito;

import jakarta.transaction.Transactional;

@Service
public class CarritoService 
{
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RepositoryCarrito carritoRepository;

    private static final String PRODUCTO_API = "http://localhost:8005/producto";
    private static final String USUARIO_API = "http://localhost:8001/usuario";
    private static final String ALERTA_API = "http://localhost:8002/alerta";
    private static final String SUCURSAL_API = "http://localhost:8008/sucursal";

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

    public boolean existeCarritoPorId(int id) 
    {
        return carritoRepository.existsById(id);
    }

    @Transactional
    public Carrito guardar(Carrito carrito)
    {
        carrito.setListaProductos(normalizarCarrito(carrito.getListaProductos()));

        Integer total = calcularTotal(carrito);
        carrito.setTotal(total);

        Usuario usuario = getUsuario(carrito.getUsuarioId());

        Sucursal sucursal = getSucursal(carrito.getSucursalId());

        Carrito nuevo = carritoRepository.save(carrito);

        crearAlerta("Carrito registrado - Dueño: " + usuario.getNombreUsuario() + " - Sucursal: " + sucursal.getNombreSucursal(), "Aviso: Carrito");


        return nuevo;
    }

    @Transactional
    public Carrito actualizar(int id, Carrito carritoActualizado)
    {
        Carrito carritoActual = carritoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));

        carritoActual.setListaProductos(normalizarCarrito(carritoActualizado.getListaProductos()));

        return carritoRepository.save(carritoActual);
    }

    @Transactional
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
            Producto productoExtraido = getProducto(producto.getProductoId());

            if (productoExtraido != null)
            {
                total += productoExtraido.getPrecio() * producto.getCantidad();
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
        // ID - PRODUCTO DEL CARRITO
        Map<Integer, CarritoProducto> mapaCarritos = new HashMap<>();

        for (CarritoProducto producto : productosCarrito) 
        {
            int id = producto.getProductoId();

            if (mapaCarritos.containsKey(id))
            {
                CarritoProducto existente = mapaCarritos.get(id);
                existente.setCantidad(existente.getCantidad()+producto.getCantidad());
            }
            else
                mapaCarritos.put(id, producto);
        }

        // Creamos la Lista, devuelve todos los valores (objetos del carrito)
        List<CarritoProducto> productos = new ArrayList<>(mapaCarritos.values());

        return productos;
    }

    private Usuario getUsuario(int usuarioId) 
    {
        Usuario usuario = restTemplate.getForObject(USUARIO_API + "/" + usuarioId, Usuario.class);

        if (usuario == null)
            throw new NoSuchElementException("Usuario no encontrado con ID: " + usuarioId);

        return usuario;
    }

    private Producto getProducto(int productoId) 
    {
        Producto producto = restTemplate.getForObject(PRODUCTO_API + "/" + productoId, Producto.class);

        if (producto == null)
            throw new NoSuchElementException("Producto no encontrado con ID: " + productoId);

        return producto;
    }

    private Sucursal getSucursal(int sucursalId) 
    {
        Sucursal sucursal = restTemplate.getForObject(SUCURSAL_API + "/" + sucursalId, Sucursal.class);

        if (sucursal == null)
            throw new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId);

        return sucursal;
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
