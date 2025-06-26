package com.patitofeliz.main.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.patitofeliz.main.model.conexion.sucursal.Sucursal;
import com.patitofeliz.main.model.conexion.usuario.Usuario;

@Service
public class SucursalServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String SUCURSAL_API = "http://localhost:8008/sucursal";

    private Sucursal getSucursal(int sucursalId) 
    {
        Sucursal sucursal = restTemplate.getForObject(SUCURSAL_API + "/" + sucursalId, Sucursal.class);

        if (sucursal == null)
            throw new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId);

        return sucursal;
    }

    public Sucursal obtenerSucursalSeguro(int sucursalId) 
    {
        try 
        {
            return getSucursal(sucursalId);
        } 
        catch (NoSuchElementException e) 
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con ID: " + sucursalId);
        }
    }   
}
