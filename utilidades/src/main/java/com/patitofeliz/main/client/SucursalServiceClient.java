package com.patitofeliz.main.client;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.inventario.ProductoInventario;
import com.patitofeliz.main.model.conexion.sucursal.Sucursal;
import com.patitofeliz.main.model.dto.sucursal.SucursalInventarioDTO;

@Service
public class SucursalServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String SUCURSAL_API = "http://localhost:8008/sucursal";

    public Sucursal getSucursal(int sucursalId) 
    {
        Sucursal sucursal = restTemplate.getForObject(SUCURSAL_API + "/" + sucursalId, Sucursal.class);

        if (sucursal == null)
            throw new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId);

        return sucursal;
    } 

    public SucursalInventarioDTO agregarProductosInventario(int sucursalId, List<ProductoInventario> listaProductos)
    {
        SucursalInventarioDTO resultado = restTemplate.postForObject(SUCURSAL_API+"/productos/"+sucursalId, listaProductos, SucursalInventarioDTO.class);

        return resultado;
    }
}
