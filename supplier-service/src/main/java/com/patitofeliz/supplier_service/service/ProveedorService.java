package com.patitofeliz.supplier_service.service;

import java.util.List;

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

        alertaServiceClient.crearAlerta("Proveedor registrado: ", TIPO_AVISO);

        return nuevo;
    }

    @Transactional
    public void borrar(int id)
    {
        alertaServiceClient.crearAlerta("Usuario Eliminado ID: "+getProveedor(id).getId(), TIPO_AVISO);
        proveedorRepository.deleteById(id);
    }
}
