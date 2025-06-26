package com.patitofeliz.main.client;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.venta.Venta;


@Service
public class VentaServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String SALES_API = "http://localhost:8007/venta";

    public List<Venta> getVentasByUsuarioId(int id)
    {
        List<Venta> listaCarritoPorId = restTemplate.getForObject(SALES_API+"/usuario/"+id, List.class);

        if (listaCarritoPorId == null || listaCarritoPorId.isEmpty())
            throw new NoSuchElementException("No se encontraron ventas para el usuario con ID: " + id);

        return listaCarritoPorId;
    }
}
