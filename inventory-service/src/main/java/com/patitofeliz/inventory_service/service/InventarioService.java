package com.patitofeliz.inventory_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.inventory_service.model.Inventario;
import com.patitofeliz.inventory_service.model.ProductoInventario;
import com.patitofeliz.inventory_service.repository.InventarioRepository;

import jakarta.transaction.Transactional;

@Service
public class InventarioService 
{
    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> getInventarios()
    {
        return inventarioRepository.findAll();
    }

    public Inventario getInventario(int id)
    {
        return inventarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public Inventario guardarInventario(Inventario inventario)
    {
        inventario.setListaProductos(normalizarInventario(inventario.getListaProductos()));

        return inventarioRepository.save(inventario);
    }

    @Transactional
    public Inventario agregarProductoInventario (int id, List<ProductoInventario> productos)
    {
        Inventario inventarioActual = inventarioRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Inventario no encontrado"));

        List<ProductoInventario> inventarioProductos = inventarioActual.getListaProductos();

        inventarioProductos.addAll(normalizarInventario(productos));

        // Reseteamos el inventario
        inventarioActual.setListaProductos(inventarioProductos);

        return inventarioRepository.save(inventarioActual);
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
