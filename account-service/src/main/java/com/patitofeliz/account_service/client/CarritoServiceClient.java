package com.patitofeliz.account_service.client;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.account_service.model.conexion.carrito.Carrito;

@Service
public class CarritoServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String CARRITO_API = "http://localhost:8004/carrito";

    public List<Carrito> getCarritoByUsuarioId(int id)
    {
        List<Carrito> listaCarritoPorId = restTemplate.getForObject(CARRITO_API+"/usuario/"+id, List.class);

        if (listaCarritoPorId == null || listaCarritoPorId.isEmpty())
            throw new NoSuchElementException("No se encontraron carritos para el usuario con ID: " + id);

        return listaCarritoPorId;
    }
}
