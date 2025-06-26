package com.patitofeliz.sucursal_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.main.client.AccountServiceClient;
import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.CarritoServiceClient;
import com.patitofeliz.main.client.InventoryServiceClient;
import com.patitofeliz.main.client.VentaServiceClient;
import com.patitofeliz.main.model.conexion.carrito.Carrito;
import com.patitofeliz.main.model.conexion.inventario.Inventario;
import com.patitofeliz.main.model.conexion.inventario.ProductoInventario;
import com.patitofeliz.main.model.conexion.usuario.Usuario;
import com.patitofeliz.main.model.conexion.venta.Venta;
import com.patitofeliz.main.model.dto.SucursalInventarioDTO;
import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.repository.SucursalRepository;

import jakarta.transaction.Transactional;

@Service
public class SucursalService 
{
    @Autowired
    private SucursalRepository sucursalRepository;
    @Autowired
    private AlertaServiceClient alertaServiceClient;
    @Autowired
    private InventoryServiceClient inventoryServiceClient;
    @Autowired
    private AccountServiceClient accountServiceClient;
    @Autowired
    private VentaServiceClient ventaServiceClient;
    @Autowired
    private CarritoServiceClient carritoServiceClient;

    public List<Sucursal> listarSucursales()
    {
        return sucursalRepository.findAll();
    }

    public Sucursal listarSucursal(int id)
    {
        return sucursalRepository.findById(id).orElse(null);
    }

    public List<Venta> listarVentasSucursal(int sucursalId)
    {
        return ventaServiceClient.getVentasPorSucursal(sucursalId);
    }

    public List<Carrito> listarCarritosSucursal(int sucursalId)
    {
        return carritoServiceClient.getCarritosPorSucursal(sucursalId);
    }

    public SucursalInventarioDTO obtenerSucursalConInventario(int sucursalId) 
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada"));

        Inventario inventario = inventoryServiceClient.getInventario(sucursal.getInventarioId());

        return new SucursalInventarioDTO(sucursal.getId(), inventario);
    }

    public boolean existePorId(int id) 
    {
        return sucursalRepository.existsById(id);
    }

    public List<Integer> listarEmpleadosSucursal(int sucursalId)
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada"));

        return sucursal.getListaEmpleados();
    }
    
    @Transactional
    public Sucursal guardar(Sucursal sucursal)
    {
        Usuario gerente = accountServiceClient.getUsuario(sucursal.getGerenteId());

        if (!gerente.getTipoUsuario().equalsIgnoreCase("gerente"))
            throw new IllegalArgumentException("El usuario asignado no tiene rol de gerente.");

        Inventario nuevoInventario = inventoryServiceClient.postInventario();

        sucursal.setInventarioId(nuevoInventario.getId());

        sucursal.setListaEmpleados(new ArrayList<>());

        Sucursal sucursalGuardar = sucursalRepository.save(sucursal);

        if (sucursalGuardar.getNombreSucursal() == null || sucursalGuardar.getNombreSucursal().isBlank()) 
        {
            String nombreGenerado = "Perfulandia " + sucursalGuardar.getId();
            sucursalGuardar.setNombreSucursal(nombreGenerado);
            sucursalGuardar = sucursalRepository.save(sucursalGuardar);
        }

        alertaServiceClient.crearAlerta("Sucursal creada: "+sucursalGuardar.getNombreSucursal()+" - Inventario Asociado: "+nuevoInventario.getId(), "Aviso: Sucursal");

        return sucursalGuardar;
    }

    @Transactional
    public List<Integer> añadirEmpleadoSucursal(int sucursalId, int idEmpleado)
    {
        List<Integer> listaEmpleados = listarSucursal(sucursalId).getListaEmpleados();

        listaEmpleados.add(idEmpleado);

        return listaEmpleados;
    }

    @Transactional
    public List<Integer> añadirEmpleadosSucursal(int sucursalId, List<Integer> listaIds)
    {
        List<Integer> listaEmpleados = listarSucursal(sucursalId).getListaEmpleados();

        listaEmpleados.addAll(listaIds);

        return listaEmpleados;
    }

    @Transactional
    public Inventario añadirProductosSucursal(int sucursalId, List<ProductoInventario> listaProductos) 
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId));

        if (listaProductos == null || listaProductos.isEmpty()) 
            throw new IllegalArgumentException("La lista de productos no puede estar vacía.");
            
        inventoryServiceClient.agregarProductosInventario(sucursal.getInventarioId(), listaProductos);

        alertaServiceClient.crearAlerta("Sucursal ID: " + sucursal.getId() +" - Se agregaron productos al inventario ID: " + sucursal.getInventarioId(),"Aviso: Sucursal");

        return inventoryServiceClient.getInventario(sucursal.getInventarioId());
    }

    @Transactional
    public void borrar(int id)
    {
        sucursalRepository.deleteById(id);
    }
}
