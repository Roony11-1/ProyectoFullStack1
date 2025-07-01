package com.patitofeliz.supplier_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.ProductoServiceClient;
import com.patitofeliz.main.client.SucursalServiceClient;
import com.patitofeliz.main.model.conexion.producto.Producto;
import com.patitofeliz.supplier_service.model.Pedido;
import com.patitofeliz.supplier_service.model.ProductoPedido;
import com.patitofeliz.supplier_service.model.Proveedor;
import com.patitofeliz.supplier_service.repository.PedidoRepository;
import com.patitofeliz.supplier_service.service.*;

public class PedidoServiceTest 
{

    @Mock 
    private PedidoRepository pedidoRepository;

    @Mock 
    private AlertaServiceClient alertaServiceClient;

    @Mock 
    private ProveedorService proveedorService;

    @Mock 
    private ProductoServiceClient productoServiceClient;

    @Mock 
    private SucursalServiceClient sucursalServiceClient;

    @InjectMocks private PedidoService pedidoService;

    @BeforeEach
    public void setup() 
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() 
    {
        List<ProductoPedido> productos = List.of(new ProductoPedido(1, 10));
        Pedido p1 = new Pedido(1, 1, 1, "fecha", productos, 1);
        Pedido p2 = new Pedido(2, 2, 2, "fecha", productos, 2);

        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Pedido> resultado = pedidoService.getPedidos();

        assertEquals(2, resultado.size());
        assertEquals(p1, resultado.get(0));
        assertEquals(p2, resultado.get(1));
    }

    @Test
    public void testGetPedido_found() 
    {
        Pedido p = new Pedido(1, 1, 1, "fecha", List.of(), 1);
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(p));

        Pedido resultado = pedidoService.getPedido(1);

        assertEquals(p, resultado);
    }

    @Test
    public void testGetPedido_notFound() 
    {
        when(pedidoRepository.findById(999)).thenReturn(Optional.empty());

        Pedido resultado = pedidoService.getPedido(999);

        assertNull(resultado);
    }

    @Test
    public void testExistePorId_true() 
    {
        when(pedidoRepository.existsById(1)).thenReturn(true);

        assertTrue(pedidoService.existePorId(1));
    }

    @Test
    public void testExistePorId_false() 
    {
        when(pedidoRepository.existsById(999)).thenReturn(false);

        assertFalse(pedidoService.existePorId(999));
    }

    @Test
    public void testGuardar() 
    {
        List<ProductoPedido> productos = List.of(new ProductoPedido(1, 10));
        Pedido pedido = new Pedido(1, 1, 1, "", productos, 0);

        when(proveedorService.getProveedor(1)).thenReturn(new Proveedor(1, "Proveedor X"));
        when(productoServiceClient.getProducto(1)).thenReturn(new Producto(1, "Prod", "Marca", 1, 1000));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(alertaServiceClient).crearAlerta(anyString(), anyString());

        Pedido resultado = pedidoService.guardar(pedido);

        assertEquals(1, resultado.getIdProveedor());
        assertEquals(1, resultado.getIdSucursal());
        assertEquals(1, resultado.getEstadoPedido());
        assertEquals(1, resultado.getListaProductos().size());
        assertNotNull(resultado.getFechaPeticion());
    }

    @Test
    public void testGuardarLote() 
    {
        Pedido pedido = new Pedido(1, 1, 1, "", List.of(), 1);
        List<Pedido> pedidos = List.of(pedido);

        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        List<Pedido> resultado = pedidoService.guardarLote(pedidos);

        assertEquals(1, resultado.size());
        assertEquals(pedido, resultado.get(0));
    }

    @Test
    public void testGuardarLote_vacio() 
    {
        List<Pedido> resultado = pedidoService.guardarLote(new ArrayList<>());

        assertEquals(0, resultado.size());
    }

    @Test
    public void testBorrar_existente() 
    {
        when(pedidoRepository.existsById(1)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(1);
        doNothing().when(alertaServiceClient).crearAlerta(anyString(), anyString());

        assertDoesNotThrow(() -> pedidoService.borrar(1));
    }

    @Test
    public void testBorrar_noExistente() 
    {
        when(pedidoRepository.existsById(999)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> pedidoService.borrar(999));
    }

    @Test
    public void testActualizaPedido_estadoValido() 
    {
        Pedido existente = new Pedido(1, 1, 1, "fecha", List.of(), 1);
        Pedido actualizado = new Pedido(1, 1, 1, "fecha", List.of(), 2);

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(alertaServiceClient).crearAlerta(anyString(), anyString());

        Pedido resultado = pedidoService.actualizaPedido(1, actualizado);

        assertEquals(2, resultado.getEstadoPedido());
    }

    @Test
    public void testActualizaPedido_estadoInvalido() 
    {
        Pedido existente = new Pedido(1, 1, 1, "fecha", List.of(), 2);
        Pedido actualizado = new Pedido(1, 1, 1, "fecha", List.of(), 1); // menor

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> pedidoService.actualizaPedido(1, actualizado));
    }

    @Test
    public void testActualizaPedido_entregado() 
    {
        Pedido existente = new Pedido(1, 1, 1, "fecha", List.of(), 3);
        Pedido actualizado = new Pedido(1, 1, 1, "fecha", List.of(), 3);

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(existente));

        assertThrows(IllegalStateException.class, () -> pedidoService.actualizaPedido(1, actualizado));
    }
}
