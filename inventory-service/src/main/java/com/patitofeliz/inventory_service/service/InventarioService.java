package com.patitofeliz.inventory_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.inventory_service.model.Inventario;
import com.patitofeliz.inventory_service.model.ProductoInventario;
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
    public Inventario agregarProductoInventario (int id, List<ProductoInventario> productos)
    {
        Inventario inventarioActual = getInventarioPorId(id);
        List<ProductoInventario> inventarioProductos = inventarioActual.getListaProductos();

        inventarioProductos.addAll(normalizarInventario(productos));

        // Reseteamos el inventario
        inventarioActual.setListaProductos(inventarioProductos);

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

        return inventarioRepository.save(inventarioActual);
    }

    @Transactional
    public Inventario guardarInventario(Inventario inventario)
    {
        // Creamos un inventario vacío siempre!
        inventario.setListaProductos(new ArrayList<>());

        return inventarioRepository.save(inventario);
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
}
