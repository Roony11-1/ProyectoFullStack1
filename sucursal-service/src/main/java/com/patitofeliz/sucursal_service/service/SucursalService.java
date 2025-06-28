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
import com.patitofeliz.main.client.PedidosServiceClient;
import com.patitofeliz.main.client.ProductoServiceClient;
import com.patitofeliz.main.client.ProveedorServiceClient;
import com.patitofeliz.main.client.VentaServiceClient;
import com.patitofeliz.main.model.conexion.carrito.Carrito;
import com.patitofeliz.main.model.conexion.inventario.Inventario;
import com.patitofeliz.main.model.conexion.inventario.ProductoInventario;
import com.patitofeliz.main.model.conexion.producto.Producto;
import com.patitofeliz.main.model.conexion.proveedor.Pedido;
import com.patitofeliz.main.model.conexion.proveedor.Proveedor;
import com.patitofeliz.main.model.conexion.usuario.Usuario;
import com.patitofeliz.main.model.conexion.venta.Venta;
import com.patitofeliz.main.model.dto.sucursal.SucursalInventarioDTO;
import com.patitofeliz.main.model.dto.sucursal.SucursalListaEnterosDTO;
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
    @Autowired
    private ProveedorServiceClient proveedorServiceClient;
    @Autowired
    private ProductoServiceClient productoServiceClient;
    @Autowired
    private PedidosServiceClient pedidosServiceClient;


    private static final String TIPO_AVISO = "Sucursal";

    public List<Sucursal> getSucursales()
    {
        return sucursalRepository.findAll();
    }

    public Sucursal getSucursal(int id)
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

    public boolean existePorId(int id) 
    {
        return sucursalRepository.existsById(id);
    }

    public SucursalInventarioDTO obtenerSucursalConInventario(int sucursalId) 
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada"));

        Inventario inventario = inventoryServiceClient.getInventario(sucursal.getInventarioId());

        return new SucursalInventarioDTO(sucursal.getId(), inventario);
    }

    public List<Integer> listarEmpleadosSucursal(int sucursalId)
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada"));

        return sucursal.getListaEmpleados();
    }

    public List<Integer> listarPedidosSucursal(int pedidoId)
    {
        Sucursal sucursal = sucursalRepository.findById(pedidoId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada"));

        return sucursal.getListaPedidos();
    }

    public List<Integer> getProveedoresSucursal(int sucursalId)
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada"));

        return sucursal.getListaProveedores();
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
        sucursal.setListaProveedores(new ArrayList<>());
        sucursal.setListaPedidos(new ArrayList<>());

        Sucursal sucursalGuardar = sucursalRepository.save(sucursal);

        if (sucursalGuardar.getNombreSucursal() == null || sucursalGuardar.getNombreSucursal().isBlank()) 
        {
            String nombreGenerado = "Perfulandia " + sucursalGuardar.getId();
            sucursalGuardar.setNombreSucursal(nombreGenerado);
            sucursalGuardar = sucursalRepository.save(sucursalGuardar);
        }

        alertaServiceClient.crearAlerta("Sucursal creada: "+sucursalGuardar.getNombreSucursal()+" - Inventario Asociado: "+nuevoInventario.getId(), TIPO_AVISO);

        return sucursalGuardar;
    }

    @Transactional
    public SucursalListaEnterosDTO añadirProveedorSucursal(int sucursalId, int idProveedor)
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId));

        Proveedor proveedor = proveedorServiceClient.getProveedor(idProveedor);
            
        List<Integer> listaProveedores = sucursal.getListaProveedores();

        if (listaProveedores.contains(idProveedor))
            throw new IllegalArgumentException("El empleado ya está asignado a la sucursal.");

        listaProveedores.add(idProveedor);

        alertaServiceClient.crearAlerta("Proveedor asignado a la sucursal ID: " + sucursal.getId() + " - ID proveedor Asociado: " + proveedor.getId(), TIPO_AVISO);

        sucursalRepository.save(sucursal);

        return new SucursalListaEnterosDTO(sucursalId, listaProveedores);
    }

    @Transactional
    public SucursalListaEnterosDTO añadirEmpleadoSucursal(int sucursalId, int idEmpleado)
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId));
            
        List<Integer> listaEmpleados = sucursal.getListaEmpleados();

        if (listaEmpleados.contains(idEmpleado))
            throw new IllegalArgumentException("El empleado ya está asignado a la sucursal.");

        listaEmpleados.add(idEmpleado);

        alertaServiceClient.crearAlerta("Empleado añadido a la sucursal ID: " + sucursal.getId() + " - ID Empleado Asociado: " + idEmpleado, TIPO_AVISO);

        sucursalRepository.save(sucursal);

        return new SucursalListaEnterosDTO(sucursalId, listaEmpleados);
    }

    @Transactional
    public SucursalListaEnterosDTO añadirEmpleadosSucursal(int sucursalId, List<Integer> listaIds)
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId));

        List<Integer> listaEmpleados = sucursal.getListaEmpleados();

        for (Integer idEmpleado : listaIds) 
        {
            if (!listaEmpleados.contains(idEmpleado)) 
            {
                listaEmpleados.add(idEmpleado);
                alertaServiceClient.crearAlerta("Empleado añadido a la sucursal ID: " + sucursal.getId() + " - ID Empleado Asociado: " + idEmpleado, TIPO_AVISO);
            } 
            else
                System.out.println("El empleado: " + idEmpleado + " ya está en la sucursal");
        }

        sucursalRepository.save(sucursal);

        return new SucursalListaEnterosDTO(sucursalId, listaEmpleados);
    }

    @Transactional
    public SucursalInventarioDTO añadirProductosSucursal(int sucursalId, List<ProductoInventario> listaProductos) 
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId));

        if (listaProductos == null || listaProductos.isEmpty()) 
            throw new IllegalArgumentException("La lista de productos no puede estar vacía.");
        
        for (ProductoInventario productoInventario : listaProductos) 
        {
            Producto producto = productoServiceClient.getProducto(productoInventario.getProductoId());
            try
            {
                añadirProveedorSucursal(sucursalId, producto.getIdProveedor());
            }
            catch (Exception e)
            {
                // No hace nada wuajaaj
            }
        }
        inventoryServiceClient.agregarProductosInventario(sucursal.getInventarioId(), listaProductos);

        alertaServiceClient.crearAlerta("Sucursal ID: " + sucursal.getId() +" - Se agregaron productos al inventario ID: " + sucursal.getInventarioId(),TIPO_AVISO);

        return new SucursalInventarioDTO(sucursalId, inventoryServiceClient.getInventario(sucursal.getInventarioId()));
    }

    @Transactional
    public Pedido añadirPedidoSucursal(int sucursalId, Pedido pedido)
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId));

        Pedido pedidoCreado = pedidosServiceClient.crearPedido(pedido);
        sucursal.getListaPedidos().add(pedidoCreado.getId());

        alertaServiceClient.crearAlerta("Sucursal ID: " + sucursal.getId() +" - Se agrego el pedido ID: " + pedidoCreado.getId(),TIPO_AVISO);

        sucursalRepository.save(sucursal);

        return pedidoCreado;
    }

    @Transactional
    public void borrar(int id)
    {
        if (!existePorId(id))
            throw new NoSuchElementException("No se encontró la sucursal con ID: " + id);
    
        sucursalRepository.deleteById(id);

        alertaServiceClient.crearAlerta("Sucursal borrada - ID: "+id, TIPO_AVISO);
    }
}
