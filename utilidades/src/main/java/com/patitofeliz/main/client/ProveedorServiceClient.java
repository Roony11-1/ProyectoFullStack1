package com.patitofeliz.main.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.proveedor.Proveedor;

@Service
public class ProveedorServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String PROVEEDOR_API = "http://localhost:8009/proveedor";

    public Proveedor getUsuario(int proveedorId) 
    {
        Proveedor proveedor = restTemplate.getForObject(PROVEEDOR_API + "/" + proveedorId, Proveedor.class);

        if (proveedor == null)
            throw new NoSuchElementException("Proveedor no encontrado con ID: " + proveedorId);

        return proveedor;
    }
}
