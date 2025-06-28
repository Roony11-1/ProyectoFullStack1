package com.patitofeliz.supplier_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.supplier_service.model.Proveedor;
import com.patitofeliz.supplier_service.repository.ProveedorRepository;

import jakarta.transaction.Transactional;

@Service
public class ProveedorService 
{
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private AlertaServiceClient alertaServiceClient;

    private static final String TIPO_AVISO = "Proveedor";

    public List<Proveedor> getProveedores()
    {
        return proveedorRepository.findAll();
    }

    public Proveedor getProveedor(int id)
    {
        return proveedorRepository.findById(id).orElse(null);
    }

    public boolean existePorId(int id) 
    {
        return proveedorRepository.existsById(id);
    }

    @Transactional
    public Proveedor guardar(Proveedor proveedor)
    {
        Proveedor nuevo = proveedorRepository.save(proveedor);

        alertaServiceClient.crearAlerta("Proveedor registrado ID: "+proveedor.getId(), TIPO_AVISO);

        return nuevo;
    }

    public List<Proveedor> guardarLote(List<Proveedor> proveedores)
    {
        List<Proveedor> listaRegistrados = new ArrayList<>();

        for (Proveedor iteradorProveedores : proveedores) 
        {
            Proveedor proveedorRegistrado = proveedorRepository.save(iteradorProveedores);
            listaRegistrados.add(proveedorRegistrado);
        }

        return listaRegistrados;
    }

    @Transactional
    public void borrar(int id)
    {
        if (!existePorId(id))
            throw new NoSuchElementException("No se encontró el Proveedor con ID: " + id);

        proveedorRepository.deleteById(id);

        alertaServiceClient.crearAlerta("Proveedor borrado - ID: "+id, TIPO_AVISO);
    }

    @Transactional
    public Proveedor actualizaProveedor(int id, Proveedor proveedorActualizado)
    {
        Proveedor proveedorActual = proveedorRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Proveedor no encontrado"));

        // lo que sea qque tenga de atributos
        
        alertaServiceClient.crearAlerta("Usuario Actualizado ID: "+proveedorActual.getId(), TIPO_AVISO);

        return proveedorRepository.save(proveedorActual);
    }
}
