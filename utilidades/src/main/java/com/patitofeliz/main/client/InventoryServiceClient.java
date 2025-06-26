package com.patitofeliz.main.client;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.patitofeliz.main.model.conexion.inventario.Inventario;
import com.patitofeliz.main.model.conexion.inventario.ProductoInventario;

@Service
public class InventoryServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String INVENTARIO_API = "http://localhost:8004/inventarios";

    private Inventario getInventario(int inventarioId) 
    {
        Inventario inventario = restTemplate.getForObject(INVENTARIO_API + "/" + inventarioId, Inventario.class);

        if (inventario == null)
            throw new NoSuchElementException("Inventario no encontrado con ID: " + inventarioId);

        return inventario;
    }

    public Inventario obtenerInventarioSeguro(int inventarioId) 
    {
        try 
        {
            return getInventario(inventarioId);
        } 
        catch (NoSuchElementException e) 
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventario no encontrado con ID: " + inventarioId);
        }
    }

    public void descontarProductoEnInventario(int inventarioId, List<ProductoInventario> productoInventario) 
    {
        restTemplate.put(INVENTARIO_API + "/" + inventarioId + "/productos", productoInventario);
    }
}
