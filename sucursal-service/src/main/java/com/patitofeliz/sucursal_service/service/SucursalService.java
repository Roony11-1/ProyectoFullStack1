package com.patitofeliz.sucursal_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.model.conexion.Alerta;
import com.patitofeliz.sucursal_service.model.conexion.Inventario;
import com.patitofeliz.sucursal_service.model.conexion.ProductoInventario;
import com.patitofeliz.sucursal_service.model.conexion.Usuario;
import com.patitofeliz.sucursal_service.model.conexion.Venta;
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
    private static final String USUARIO_API = "http://localhost:8001/usuario";
    private static final String VENTAS_API = "http://localhost:8007/venta";

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
        return getVentasPorSucursal(sucursalId);
    }

    public Inventario listarInventarioSucursal(int sucursalId)
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada"));

        Inventario inventario = getInventario(sucursal.getInventarioId());

        return inventario;
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
        Usuario gerente = getUsuario(sucursal.getGerenteId());

        if (!gerente.getTipoUsuario().equalsIgnoreCase("gerente"))
            throw new IllegalArgumentException("El usuario asignado no tiene rol de gerente.");

        Inventario nuevoInventario = postInventario();

        sucursal.setInventarioId(nuevoInventario.getId());

        sucursal.setListaEmpleados(new ArrayList<>());

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

    @Transactional
    public Inventario añadirProductosSucursal(int sucursalId, List<ProductoInventario> listaProductos) 
    {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId));

        if (listaProductos == null || listaProductos.isEmpty()) 
            throw new IllegalArgumentException("La lista de productos no puede estar vacía.");
            
        agregarProductosInventario(sucursal.getInventarioId(), listaProductos);

        crearAlerta("Sucursal ID: " + sucursal.getId() +" - Se agregaron productos al inventario ID: " + sucursal.getInventarioId(),"Aviso: Sucursal");

        return getInventario(sucursal.getInventarioId());
    }

    @Transactional
    public void borrar(int id)
    {
        sucursalRepository.deleteById(id);
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

    private Inventario agregarProductosInventario(int inventarioId, List<ProductoInventario> productos) 
    {
        Inventario inventario = restTemplate.postForObject(INVENTARIO_API + "/" + inventarioId + "/productos", productos, Inventario.class);

        return inventario;
    }

        private Inventario getInventario(int inventarioId) 
    {
        Inventario inventario = restTemplate.getForObject(INVENTARIO_API + "/" + inventarioId, Inventario.class);

        if (inventario == null)
            throw new NoSuchElementException("Inventario no encontrado con ID: " + inventarioId);

        return inventario;
    }

    private List<Venta> getVentasPorSucursal(int sucursalId)
    {
        List<Venta> ventasSucursalId = restTemplate.getForObject(VENTAS_API+"/sucursal/"+sucursalId, List.class);

        if (ventasSucursalId == null || ventasSucursalId.isEmpty())
            throw new NoSuchElementException("Esta sucursal no tiene ventas asociadas");

        return ventasSucursalId;
    }

    private Usuario getUsuario(int usuarioId) 
    {
        Usuario usuario = restTemplate.getForObject(USUARIO_API + "/" + usuarioId, Usuario.class);

        if (usuario == null)
            throw new NoSuchElementException("Usuario no encontrado con ID: " + usuarioId);

        return usuario;
    }
}
