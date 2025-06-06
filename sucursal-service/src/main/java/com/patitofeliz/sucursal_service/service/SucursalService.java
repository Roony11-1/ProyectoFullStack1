package com.patitofeliz.sucursal_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.model.conexion.Alerta;
import com.patitofeliz.sucursal_service.model.conexion.Inventario;
import com.patitofeliz.sucursal_service.repository.SucursalRepository;

import jakarta.transaction.Transactional;

@Service
public class SucursalService 
{
    @Autowired
    private SucursalRepository sucursalRepository;
    @Autowired
    private RestTemplate restTemplate;

    private static final String ALERTA_API = "http://localhost:8002/alerta";
    private static final String INVENTARIO_API = "http://localhost:8004/inventarios";

    public List<Sucursal> listarSucursales()
    {
        return sucursalRepository.findAll();
    }

    public Sucursal listarSucursal(int id)
    {
        return sucursalRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Sucursal guardar(Sucursal sucursal)
    {
        Inventario nuevoInventario = postInventario();

        sucursal.setInventarioId(nuevoInventario.getId());

        Sucursal sucursalGuardar = sucursalRepository.save(sucursal);

        if (sucursalGuardar.getNombreSucursal() == null || sucursalGuardar.getNombreSucursal().isBlank()) 
        {
            String nombreGenerado = "Perfulandia " + sucursalGuardar.getId();
            sucursalGuardar.setNombreSucursal(nombreGenerado);
            sucursalGuardar = sucursalRepository.save(sucursalGuardar);
        }

        crearAlerta("Sucursal creada: "+sucursalGuardar.getNombreSucursal()+" - Inventario Asociado: "+nuevoInventario.getId(), "Aviso: Sucursal");

        return sucursalGuardar;
    }

    // Auxiliares

    private void crearAlerta(String mensaje, String tipoAlerta)
    {
        Alerta alertaProductoRegistrado = new Alerta(mensaje, tipoAlerta);

        try
        {
            restTemplate.postForObject(ALERTA_API, alertaProductoRegistrado, Alerta.class);
        }
        catch (RestClientException e)
        {
            throw new IllegalArgumentException("No se pudo ingresar la Alerta: "+e);
        }
    }

    private Inventario postInventario() 
    {
        Inventario inventario = restTemplate.postForObject(INVENTARIO_API, new Inventario(), Inventario.class);

        return inventario;
    }
}
