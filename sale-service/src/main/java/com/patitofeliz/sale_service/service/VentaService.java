package com.patitofeliz.sale_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.patitofeliz.main.client.AccountServiceClient;
import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.CarritoServiceClient;
import com.patitofeliz.main.client.InventoryServiceClient;
import com.patitofeliz.main.client.ProductoServiceClient;
import com.patitofeliz.main.client.SucursalServiceClient;
import com.patitofeliz.main.model.conexion.carrito.Carrito;
import com.patitofeliz.main.model.conexion.carrito.CarritoProducto;
import com.patitofeliz.main.model.conexion.inventario.Inventario;
import com.patitofeliz.main.model.conexion.inventario.ProductoInventario;
import com.patitofeliz.main.model.conexion.producto.Producto;
import com.patitofeliz.main.model.conexion.sucursal.Sucursal;
import com.patitofeliz.main.model.conexion.usuario.Usuario;
import com.patitofeliz.sale_service.model.Venta;
import com.patitofeliz.sale_service.model.VentaProducto;
import com.patitofeliz.sale_service.repository.VentaRepository;

@Service
public class VentaService 
{
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private AlertaServiceClient alertaServiceClient;
    @Autowired
    private CarritoServiceClient carritoServiceClient;
    @Autowired
    private AccountServiceClient accountServiceClient;
    @Autowired
    private ProductoServiceClient productoServiceClient;
    @Autowired
    private SucursalServiceClient sucursalServiceClient;
    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    private static final String TIPO_AVISO = "Venta";

    public List<Venta> getVentasPorUsuarioId(int id)
    {
        return ventaRepository.findByUsuarioId(id);
    }

    public List<Venta> getVentasPorVendedorId(int id)
    {
        return ventaRepository.findByVendedorId(id);
    }

    public List<Venta> getVentasPorSucursalId(int id)
    {
        return ventaRepository.findBySucursalId(id);
    }

    public List<Venta> getVentas()
    {
        return ventaRepository.findAll();
    }

    public Venta getVentaId(int id)
    {
        return ventaRepository.findById(id).orElse(null);
    }

    public boolean existePorId(int id) 
    {
        return ventaRepository.existsById(id);
    }

    @Transactional
    public void borrar(int id)
    {
        if (!existePorId(id))
            throw new NoSuchElementException("No se encontró la venta con ID: " + id);

        ventaRepository.deleteById(id);

        alertaServiceClient.crearAlerta("Venta borrada - ID: "+id, TIPO_AVISO);
    }

    @Transactional
    public Venta generarVenta(Venta venta) 
    {
        validarVenta(venta);

        Carrito carritoVenta = carritoServiceClient.getCarritoById(venta.getCarritoId());
        validarCarrito(carritoVenta);

        Usuario comprador = accountServiceClient.getUsuario(carritoVenta.getUsuarioId());
        Usuario vendedor = accountServiceClient.getUsuario(venta.getVendedorId());

        Sucursal sucursal = sucursalServiceClient.getSucursal(carritoVenta.getSucursalId());
        Inventario inventario = inventoryServiceClient.getInventario(sucursal.getInventarioId());

        List<VentaProducto> productosVenta = new ArrayList<>();
        List<ProductoInventario> productosActualizados = procesarCarrito(carritoVenta.getListaProductos(), inventario, productosVenta);

        inventoryServiceClient.descontarProductoEnInventario(sucursal.getInventarioId(), productosActualizados);

        venta.setListaProductos(productosVenta);
        venta.setTotal(carritoVenta.getTotal());
        venta.setUsuarioId(comprador.getId());
        venta.setSucursalId(sucursal.getId());

        carritoServiceClient.borrarCarritoPorId(carritoVenta.getId());

        Venta nuevaVenta = ventaRepository.save(venta);

        crearAlertaVenta(venta, comprador, vendedor, sucursal);

        return nuevaVenta;
    }

    private void validarVenta(Venta venta) 
    {
        if (venta == null)
            throw new IllegalArgumentException("La venta no puede ser null");
    }

    private void validarCarrito(Carrito carrito) 
    {
        if (carrito.getListaProductos().isEmpty())
            throw new NoSuchElementException("Carrito ID: " + carrito.getId() + " está vacío!");
    }

    private List<ProductoInventario> procesarCarrito(List<CarritoProducto> listaCarrito, Inventario inventario, List<VentaProducto> productosVenta) 
    {
        List<ProductoInventario> productosActualizados = new ArrayList<>();

        for (CarritoProducto item : listaCarrito) 
        {
            Producto producto = productoServiceClient.getProducto(item.getProductoId());
            ProductoInventario inventarioProducto = buscarEnInventario(inventario, item.getProductoId());

            if (item.getCantidad() > inventarioProducto.getCantidad())
                throw new NoSuchElementException("No hay suficientes existencias de " + producto.getNombre());

            inventarioProducto.setCantidad(inventarioProducto.getCantidad() - item.getCantidad());

            productosVenta.add(new VentaProducto(item.getProductoId(), item.getCantidad()));
            productosActualizados.add(inventarioProducto);
        }

        return productosActualizados;
    }

    private ProductoInventario buscarEnInventario(Inventario inventario, int productoId) 
    {
        return inventario.getListaProductos().stream().filter(p -> p.getProductoId() == productoId).findFirst()
            .orElseThrow(() -> new NoSuchElementException("Producto ID " + productoId + " no se encuentra en el inventario"));
    }

    private void crearAlertaVenta(Venta venta, Usuario comprador, Usuario vendedor, Sucursal sucursal) 
    {
        String mensaje = "Venta Carrito - id: " + venta.getCarritoId()
                    + " - comprador: " + comprador.getNombreUsuario()
                    + " - vendedor: " + vendedor.getNombreUsuario()
                    + " - Sucursal: " + sucursal.getNombreSucursal();

        alertaServiceClient.crearAlerta(mensaje, TIPO_AVISO);
    }
}
