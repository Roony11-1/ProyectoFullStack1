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
import com.patitofeliz.inventory_service.repository.InventarioRepository;
import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.ProductoServiceClient;
import com.patitofeliz.main.model.conexion.producto.Producto;

import jakarta.transaction.Transactional;

@Service
public class InventarioService 
{
    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private ProductoServiceClient productoServiceClient;
    @Autowired
    private AlertaServiceClient alertaServiceClient;


    // Inventarios
    public List<Inventario> getInventarios()
    {
        return inventarioRepository.findAll();
    }

    public Inventario getInventario(int id)
    {
        return inventarioRepository.findById(id).orElse(null);
    }

    // Metodo con excepción
    private Inventario getInventarioPorId(int id) 
    {
        return inventarioRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Inventario no encontrado"));
    }

    public boolean existePorId(int id) 
    {
        return inventarioRepository.existsById(id);
    }

    private Producto getProducto(int productoId) 
    {
        Producto producto = productoServiceClient.obtenerProductoSeguro(productoId);

        return producto;
    }

    // Agrega un solo producto al inventario
    public Inventario agregarProductoInventario (int inventarioId, ProductoInventario productoInventario)
    {
        Inventario inventarioActual = getInventarioPorId(inventarioId);
        List<ProductoInventario> inventarioProductos = inventarioActual.getListaProductos();

        Producto productoExistente = getProducto(productoInventario.getProductoId());

        if (productoExistente != null) 
        {
            inventarioProductos.add(productoInventario);
            inventarioActual.setListaProductos(normalizarInventario(inventarioProductos));

            alertaServiceClient.crearAlertaSeguro("Producto agregado al inventario, ID: " + inventarioActual.getId(), "Aviso: Inventario");

            return inventarioRepository.save(inventarioActual);
        } 
        else 
            alertaServiceClient.crearAlertaSeguro("Producto no encontrado, no fue agregado. ID Inventario: " + inventarioActual.getId(), "Aviso: Inventario");

        return inventarioActual;
    }

    @Transactional
    public Inventario agregarProductosInventario (int id, List<ProductoInventario> productos)
    {
        Inventario inventarioActual = getInventarioPorId(id);
        List<ProductoInventario> inventarioProductos = inventarioActual.getListaProductos();

        List<ProductoInventario> productosValidos = new ArrayList<>();

        for (ProductoInventario producto : productos) 
        {
            // existe el producto¿?
            Producto productoExistente = getProducto(producto.getProductoId());

            if (productoExistente != null)
                productosValidos.add(producto);
        }

        inventarioProductos.addAll(productosValidos);

        inventarioProductos = normalizarInventario(inventarioProductos);

        // Reseteamos el inventario
        inventarioActual.setListaProductos(inventarioProductos);

        alertaServiceClient.crearAlertaSeguro("Productos agregados al inventario, ID: "+inventarioActual.getId(), "Aviso: Inventario");

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

        alertaServiceClient.crearAlertaSeguro("Productos eliminados del inventario, ID:"+inventarioActual.getId(), "Aviso: Inventario");

        return inventarioRepository.save(inventarioActual);
    }

    @Transactional
    public Inventario guardarInventario(Inventario inventario)
    {
        // Creamos un inventario vacío siempre!
        inventario.setListaProductos(new ArrayList<>());

        Inventario inventarioGuardado = inventarioRepository.save(inventario);

        alertaServiceClient.crearAlertaSeguro("Inventario creado ID: "+inventarioGuardado.getId(), "Aviso: Inventario");

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

        alertaServiceClient.crearAlertaSeguro("Inventario vaciado, ID:"+inventario.getId(), "Aviso: Inventario");

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
        alertaServiceClient.crearAlertaSeguro("Cantidad de articulos del inventario modificados ID: "+inventario.getId(), "Aviso: Inventario");
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
}
