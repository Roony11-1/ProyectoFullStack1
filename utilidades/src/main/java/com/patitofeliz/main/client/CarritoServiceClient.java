package com.patitofeliz.main.client;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.carrito.Carrito;


@Service
public class CarritoServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String CARRITO_API = "http://localhost:8003/carrito";

    public List<Carrito> getCarritoByUsuarioId(int id)
    {
        List<Carrito> listaCarritoPorId = restTemplate.getForObject(CARRITO_API+"/usuario/"+id, List.class);

        if (listaCarritoPorId == null || listaCarritoPorId.isEmpty())
            throw new NoSuchElementException("No se encontraron carritos para el usuario con ID: " + id);

        return listaCarritoPorId;
    }

    public List<Carrito> getCarritosPorSucursal(int sucursalId)
    {
        List<Carrito> carritoSucursalId = restTemplate.getForObject(CARRITO_API+"/sucursal/"+sucursalId, List.class);

        if (carritoSucursalId == null || carritoSucursalId.isEmpty())
            throw new NoSuchElementException("Esta sucursal no tiene carritos asociados");

        return carritoSucursalId;
    }

    public Carrito getCarritoById(int id)
    {
        Carrito CarritoPorId = restTemplate.getForObject(CARRITO_API+"/"+id, Carrito.class);

        if (CarritoPorId == null)
            throw new NoSuchElementException("No se encontro el carrito con ID: " + id);

        return CarritoPorId;
    }

    public void borrarCarritoPorId(int id)
    {
        restTemplate.delete(CARRITO_API + "/" + id);
    }
}
