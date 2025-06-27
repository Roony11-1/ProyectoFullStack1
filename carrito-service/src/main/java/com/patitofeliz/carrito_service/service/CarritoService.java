package com.patitofeliz.carrito_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.carrito_service.model.Carrito;
import com.patitofeliz.carrito_service.model.CarritoProducto;
import com.patitofeliz.carrito_service.repository.RepositoryCarrito;
import com.patitofeliz.main.client.AccountServiceClient;
import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.ProductoServiceClient;
import com.patitofeliz.main.client.SucursalServiceClient;
import com.patitofeliz.main.model.conexion.producto.Producto;
import com.patitofeliz.main.model.conexion.sucursal.Sucursal;
import com.patitofeliz.main.model.conexion.usuario.Usuario;

import jakarta.transaction.Transactional;

@Service
public class CarritoService 
{
    @Autowired
    private RepositoryCarrito carritoRepository;
    @Autowired
    private AlertaServiceClient alertaServiceClient;
    @Autowired
    private AccountServiceClient accountServiceClient;
    @Autowired
    private ProductoServiceClient productoServiceClient;
    @Autowired
    private SucursalServiceClient sucursalServiceClient;

    private static final String TIPO_AVISO = "Carrito";

    public List<Carrito> getCarritos()
    {
        return carritoRepository.findAll();
    }

    public List<Carrito> getCarritosByUsuarioid(int id)
    {
        return carritoRepository.findByUsuarioId(id);
    }

    public List<Carrito> getCarritosBySucursalid(int id)
    {
        return carritoRepository.findBySucursalId(id);
    }

    public Carrito getCarrito(int id)
    {
        return carritoRepository.findById(id).orElse(null);
    }

    public boolean existePorId(int id) 
    {
        return carritoRepository.existsById(id);
    }

    @Transactional
    public Carrito guardar(Carrito carrito)
    {
        carrito.setListaProductos(normalizarCarrito(carrito.getListaProductos()));

        Integer total = calcularTotal(carrito);
        carrito.setTotal(total);

        Usuario usuario = accountServiceClient.getUsuario(carrito.getUsuarioId());

        Sucursal sucursal = sucursalServiceClient.getSucursal(carrito.getSucursalId());

        Carrito nuevo = carritoRepository.save(carrito);

        alertaServiceClient.crearAlerta("Carrito registrado - Dueño: " + usuario.getNombreUsuario() + " - Sucursal: " + sucursal.getNombreSucursal(), TIPO_AVISO);

        return nuevo;
    }

    @Transactional
    public Carrito actualizar(int id, Carrito carritoActualizado)
    {
        Carrito carritoActual = carritoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));

        carritoActual.setListaProductos(normalizarCarrito(carritoActualizado.getListaProductos()));

        alertaServiceClient.crearAlerta("Carrito actualizado - Dueño ID: " + carritoActual.getUsuarioId() + " - Sucursal ID: " + carritoActual.getSucursalId(), TIPO_AVISO);

        return carritoRepository.save(carritoActual);
    }

    @Transactional
    public void borrar(int id)
    {
        if (!existePorId(id))
            throw new NoSuchElementException("No se encontró el carrito con ID: " + id);

        carritoRepository.deleteById(id);

        alertaServiceClient.crearAlerta("Carrito borrado - ID: "+id, TIPO_AVISO);
    }

    private Integer calcularTotal(Carrito carrito) 
    {
        int total = 0;

        if (carrito.getListaProductos() == null || carrito.getListaProductos().isEmpty())
            return total;

        for (CarritoProducto producto : carrito.getListaProductos()) 
        {
            Producto productoExtraido = productoServiceClient.getProducto(carrito.getUsuarioId());

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
}
