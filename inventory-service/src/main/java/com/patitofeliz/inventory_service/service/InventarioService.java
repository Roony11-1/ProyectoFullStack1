package com.patitofeliz.inventory_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.inventory_service.model.Inventario;
import com.patitofeliz.inventory_service.model.ProductoInventario;
import com.patitofeliz.inventory_service.model.conexion.Alerta;
import com.patitofeliz.inventory_service.model.conexion.Producto;
import com.patitofeliz.inventory_service.repository.InventarioRepository;

import jakarta.transaction.Transactional;

@Service
public class InventarioService 
{
    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private RestTemplate restTemplate;

    private static final String PRODUCTO_API = "http://localhost:8005/producto";
    private static final String ALERTA_API = "http://localhost:8002/alerta";

    // Inventarios
    public List<Inventario> getInventarios()
    {
        return inventarioRepository.findAll();
    }

    public Inventario getInventario(int id)
    {
        return inventarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public Inventario agregarProductosInventario (int id, List<ProductoInventario> productos)
    {
        Inventario inventarioActual = getInventarioPorId(id);
        List<ProductoInventario> inventarioProductos = inventarioActual.getListaProductos();

        List<ProductoInventario> productosValidos = new ArrayList<>();

        for (ProductoInventario producto : productos) 
        {
            try 
            {
                // Consulta GET para saber si el producto existe
                Producto productoExistente = getProducto(producto.getProductoId());

                if (productoExistente != null)
                    productosValidos.add(producto);
            } 
            catch 
            (HttpClientErrorException.NotFound e) 
            {
                crearAlerta("Articulo ID: "+producto.getProductoId()+" no existe, no fue agregado!", "Aviso: Inventario");
            }
        }

        inventarioProductos.addAll(productosValidos);

        inventarioProductos = normalizarInventario(inventarioProductos);

        // Reseteamos el inventario
        inventarioActual.setListaProductos(inventarioProductos);

        crearAlerta("Productos agregados al inventario, ID: "+inventarioActual.getId(), "Aviso: Inventario");

        return inventarioRepository.save(inventarioActual);
    }

    @Transactional
    public Inventario eliminarProductoInventario(int id, List<ProductoInventario> productosEliminar) 
    {
        Inventario inventarioActual = getInventarioPorId(id);

        List<ProductoInventario> inventarioProductos = normalizarInventario(inventarioActual.getListaProductos());

        for (ProductoInventario productoEliminar : productosEliminar) 
        {
            // Iterador remueve si corresponden a las id, como si usaramos dos iteradores pero mas compacto
            inventarioProductos.removeIf(p -> p.getProductoId() == productoEliminar.getProductoId());
        }

        inventarioActual.setListaProductos(inventarioProductos);

        crearAlerta("Productos eliminados del inventario, ID:"+inventarioActual.getId(), "Aviso: Inventario");

        return inventarioRepository.save(inventarioActual);
    }

    @Transactional
    public Inventario guardarInventario(Inventario inventario)
    {
        // Creamos un inventario vacío siempre!
        inventario.setListaProductos(new ArrayList<>());

        Inventario inventarioGuardado = inventarioRepository.save(inventario);

        crearAlerta("Inventario creado ID: "+inventarioGuardado.getId(), "Aviso: Inventario");

        return inventarioGuardado;
    }

    @Transactional
    public void borrarInventario(int id)
    {
        inventarioRepository.deleteById(id);
    }

    @Transactional
    public Inventario vaciarInventario(int id) 
    {
        Inventario inventario = getInventarioPorId(id);
        inventario.setListaProductos(new ArrayList<>());

        crearAlerta("Inventario vaciado, ID:"+inventario.getId(), "Aviso: Inventario");

        return inventarioRepository.save(inventario);
    }

    // Objetos del inventario
    public List<ProductoInventario> getProductosInventarioId(int idInventario)
    {
        Inventario inventarioActual = getInventarioPorId(idInventario);

        return inventarioActual.getListaProductos();
    }

    @Transactional
    public Inventario actualizarProductosEnInventario(int idInventario, List<ProductoInventario> productosActualizados) 
    {
        Inventario inventario = getInventarioPorId(idInventario);
        List<ProductoInventario> productos = inventario.getListaProductos();

        normalizarInventario(productosActualizados);

        for (ProductoInventario productoActualizado : productosActualizados) 
        {
            for (ProductoInventario producto : productos) 
            {
                if (producto.getProductoId() == productoActualizado.getProductoId()) 
                {
                    producto.setCantidad(productoActualizado.getCantidad());
                    break;
                }
            }
        }
        inventario.setListaProductos(normalizarInventario(productos));
        crearAlerta("Cantidad de articulos del inventario modificados ID: "+inventario.getId(), "Aviso: Inventario");
        return inventarioRepository.save(inventario);
    }

    // Auxiliar
    private List<ProductoInventario> normalizarInventario(List<ProductoInventario> productosInventario)
    {
        // ID - PRODUCTO DEL inventario
        Map<Integer, ProductoInventario> mapaProductos = new HashMap<>();

        for (ProductoInventario producto : productosInventario) 
        {
            int id = producto.getProductoId();

            if (mapaProductos.containsKey(id))
            {
                ProductoInventario existente = mapaProductos.get(id);
                existente.setCantidad(existente.getCantidad()+producto.getCantidad());
            }
            else
                mapaProductos.put(id, producto);
        }

        // Creamos la Lista, devuelve todos los valores (objetos del inventario)
        List<ProductoInventario> productos = new ArrayList<>(mapaProductos.values());

        return productos;
    }

    private Inventario getInventarioPorId(int id) 
    {
        return inventarioRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Inventario no encontrado"));
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

    private Producto getProducto(int productoId) 
    {
        Producto producto = restTemplate.getForObject(PRODUCTO_API + "/" + productoId, Producto.class);

        if (producto == null)
            throw new NoSuchElementException("Producto no encontrado con ID: " + productoId);

        return producto;
    }
}
