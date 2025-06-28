package com.patitofeliz.supplier_service.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.ProductoServiceClient;
import com.patitofeliz.main.client.SucursalServiceClient;
import com.patitofeliz.main.model.conexion.inventario.ProductoInventario;
import com.patitofeliz.main.model.conexion.producto.Producto;
import com.patitofeliz.supplier_service.model.Pedido;
import com.patitofeliz.supplier_service.model.ProductoPedido;
import com.patitofeliz.supplier_service.model.Proveedor;
import com.patitofeliz.supplier_service.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService 
{
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private AlertaServiceClient alertaServiceClient;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProductoServiceClient productoServiceClient;
    @Autowired
    private SucursalServiceClient sucursalServiceClient;

    private static final String TIPO_AVISO = "Pedido";

    public List<Pedido> getPedidos()
    {
        return pedidoRepository.findAll();
    }

    public Pedido getPedido(int id)
    {
        return pedidoRepository.findById(id).orElse(null);
    }

    public boolean existePorId(int id) 
    {
        return pedidoRepository.existsById(id);
    }

    @Transactional
    public Pedido guardar(Pedido pedido)
    {
        pedido.setListaProductos(validarLista(pedido));
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        pedido.setFechaPeticion(fechaActual);
        pedido.setEstadoPedido(1);

        Pedido nuevo = pedidoRepository.save(pedido);

        alertaServiceClient.crearAlerta("Pedido registrado ID sucursal: "+pedido.getIdSucursal()+"- ID Proveedor: "+pedido.getIdProveedor(), TIPO_AVISO);

        return nuevo;
    }

    @Transactional
    public List<Pedido> guardarLote(List<Pedido> pedidos)
    {
        List<Pedido> listaRegistrados = new ArrayList<>();

        for (Pedido iteradorProveedores : listaRegistrados) 
        {
            Pedido pedidosRegistrados = pedidoRepository.save(iteradorProveedores);
            listaRegistrados.add(pedidosRegistrados);
        }
        return listaRegistrados;
    }

    @Transactional
    public void borrar(int id)
    {
        if (!existePorId(id))
            throw new NoSuchElementException("No se encontró el Pedido con ID: " + id);

        pedidoRepository.deleteById(id);

        alertaServiceClient.crearAlerta("Pedido borrado - ID: "+id, TIPO_AVISO);
    }

    @Transactional
    public Pedido actualizaPedido(int id, Pedido pedidoActualizado)
    {
        Pedido pedidoActual = pedidoRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));

        int estadoActual = pedidoActual.getEstadoPedido();
        int nuevoEstado = pedidoActualizado.getEstadoPedido();

        if (estadoActual == 3)
            throw new IllegalStateException("El pedido ya fue entregado y no puede modificarse ID: "+pedidoActual.getId());

        if (nuevoEstado < estadoActual)
            throw new IllegalArgumentException("No se puede volver a un estado anterior del pedido.");

        if (estadoActual == 1)
        {
            pedidoActual.setIdProveedor(pedidoActualizado.getIdProveedor());
            pedidoActual.setListaProductos(validarLista(pedidoActualizado));
        }
            

        pedidoActual.setIdSucursal(pedidoActualizado.getIdSucursal());
        pedidoActual.setEstadoPedido(nuevoEstado);

        if (estadoActual==3)
        {
            agregarProductosPedido(pedidoActual.getIdSucursal(), pedidoActual);
            alertaServiceClient.crearAlerta("Pedido agregado a la sucursal ID:"+pedidoActual.getIdSucursal(), TIPO_AVISO);
        }

        alertaServiceClient.crearAlerta("Pedido Actualizado ID: " + pedidoActual.getId(), TIPO_AVISO);
        
        return pedidoRepository.save(pedidoActual);
    }

    @Transactional
    private void agregarProductosPedido(int sucursalId, Pedido pedido)
    {
        List<ProductoPedido> listaPedido = pedido.getListaProductos();
        List<ProductoInventario> listaInventario = new ArrayList<>();

        // Convertimos a lo que guard el inventario
        for (ProductoPedido productoPedido : listaPedido) 
        {
            listaInventario.add(new ProductoInventario(productoPedido.getProductoId(), productoPedido.getCantidad()));
        }

        // con la lista cargada
        sucursalServiceClient.agregarProductosInventario(sucursalId, listaInventario);
    }

    private List<ProductoPedido> validarLista(Pedido pedido) 
    {
        List<ProductoPedido> listaValidada = new ArrayList<>();

        Proveedor proveedor = proveedorService.getProveedor(pedido.getIdProveedor());

        if (proveedor == null)
            throw new NoSuchElementException("Proveedor no encontrado con ID: " + pedido.getIdProveedor());

        // Validar productos
        for (ProductoPedido productoPedido : pedido.getListaProductos()) {
            try 
            {
                Producto producto = productoServiceClient.getProducto(productoPedido.getProductoId());
                if (producto.getIdProveedor() == proveedor.getId())
                    listaValidada.add(productoPedido);
            } 
            catch (Exception e) 
            {
                // me lo salto
            }
        }
        return normalizarPedido(listaValidada);
    }

    // Auxiliar
    private List<ProductoPedido> normalizarPedido(List<ProductoPedido> productosPedido)
    {
        // ID - PRODUCTO DEL inventario
        Map<Integer, ProductoPedido> mapaProductos = new HashMap<>();

        for (ProductoPedido producto : productosPedido) 
        {
            int id = producto.getProductoId();

            if (mapaProductos.containsKey(id))
            {
                ProductoPedido existente = mapaProductos.get(id);
                existente.setCantidad(existente.getCantidad()+producto.getCantidad());
            }
            else
                mapaProductos.put(id, producto);
        }

        // Creamos la Lista, devuelve todos los valores (objetos del inventario)
        List<ProductoPedido> productos = new ArrayList<>(mapaProductos.values());

        return productos;
    }
}
