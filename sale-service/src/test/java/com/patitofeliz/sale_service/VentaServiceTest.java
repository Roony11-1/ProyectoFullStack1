package com.patitofeliz.sale_service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

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
import com.patitofeliz.sale_service.service.VentaService;

public class VentaServiceTest 
{
    // Simulamos el rest para el metodo crearAlerta
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    public void setup() { MockitoAnnotations.openMocks(this); }

    @Test
    public void testGetAll()
    {
        // Lista de productos 1
        List<VentaProducto> listaProductos = new ArrayList<>();
        listaProductos.add(new VentaProducto(1, 1));
        listaProductos.add(new VentaProducto(2, 2));
        listaProductos.add(new VentaProducto(3, 3));
        listaProductos.add(new VentaProducto(4, 4));

        // Venta 1
        Venta v1 = new Venta();
        v1.setId(1);
        v1.setCarritoId(1);
        v1.setSucursalId(1);
        v1.setUsuarioId(1);
        v1.setVendedorId(1);
        v1.setListaProductos(listaProductos);
        v1.setTotal(0);

        // Venta 2
        Venta v2 = new Venta();
        v2.setId(2);
        v2.setCarritoId(1);
        v2.setSucursalId(1);
        v2.setUsuarioId(1);
        v2.setVendedorId(1);
        v2.setListaProductos(listaProductos);
        v2.setTotal(0);

        when(ventaRepository.findAll()).thenReturn(Arrays.asList(v1,v2));

        List<Venta> resultado = ventaService.getVentas();

        int contador = 1;
        for (Venta venta : resultado) 
        {
            assertEquals(contador, venta.getId());
            assertEquals(1, venta.getCarritoId());
            assertEquals(1, venta.getSucursalId());
            assertEquals(1, venta.getUsuarioId());
            assertEquals(1, venta.getVendedorId());

                int contadorProducto = 1;
                for (VentaProducto producto : venta.getListaProductos()) 
                {
                    assertEquals(contadorProducto, producto.getProductoId());
                    assertEquals(contadorProducto, producto.getCantidad());

                    contadorProducto++;
                }

            contador++;
        }
    }

    @Test
    public void testGetVentaById_NoExiste()
    {
        //simularemos que no existe un usuario con el id 99
        when(ventaRepository.findById(99)).thenReturn(Optional.empty());

        //ejecutamos el método
        Venta resultado = ventaService.getVentaId (99);

        //Verificamos el resultado
        assertNull(resultado);
    }

    @Test
    public void testGenerarVenta()
    {
        //Se crea el usuario de prueba para guardar
        Venta v1 = new Venta();
        v1.setId(1);
        v1.setUsuarioId(1);
        v1.setVendedorId(1);
        v1.setSucursalId(1);
        v1.setCarritoId(1);
        v1.setListaProductos(new ArrayList<>());
        v1.setTotal(0);

        // Mockearemos las cosas
        Usuario u1 = new Usuario();
        u1.setId(1);

        Producto p1 = new Producto();
        p1.setId(1);

        Sucursal s1 = new Sucursal();
        s1.setId(1);
        s1.setInventarioId(1);
        Inventario inv1 = new Inventario();
        inv1.setId(1);
        inv1.setListaProductos(new ArrayList<>());
        inv1.getListaProductos().add(new ProductoInventario(1, 500));

        Carrito c1 = new Carrito();
        c1.setId(1);
        c1.setListaProductos(new ArrayList<>());
        c1.getListaProductos().add(new CarritoProducto(1, 100));

        when(restTemplate.getForObject(eq("http://localhost:8001/usuario/1"),eq(Usuario.class))).thenReturn(u1);
        when(restTemplate.getForObject(eq("http://localhost:8005/producto/1"),eq(Producto.class))).thenReturn(p1);
        when(restTemplate.getForObject(eq("http://localhost:8008/sucursal/1"),eq(Sucursal.class))).thenReturn(s1);
        when(restTemplate.getForObject(eq("http://localhost:8003/carrito/1"),eq(Carrito.class))).thenReturn(c1);
        when(restTemplate.getForObject(eq("http://localhost:8004/inventarios/1"),eq(Inventario.class))).thenReturn(inv1);

        //debemos simular que el repositorio guarda y retorna el usuario
        when(ventaRepository.save(v1)).thenReturn(v1);

        // Hay que mockear el obtener weas de los rest, me lo dejo pa mas rato que paja
        Venta resultado = ventaService.generarVenta(v1);

        // Verificamos la venta
        assertEquals(1, resultado.getId());
        assertEquals(1, resultado.getUsuarioId());
        assertEquals(1, resultado.getVendedorId());
        assertEquals(1, resultado.getSucursalId());
        assertEquals(1, resultado.getCarritoId());
        assertEquals(new ArrayList<>(List.of(new CarritoProducto(1,  100))), resultado.getListaProductos());
        assertEquals(0, resultado.getTotal());
    }

    @Test
    public void testBorrarVenta() 
    {
        Venta v = new Venta();
        v.setId(1);

        when(ventaRepository.findById(1)).thenReturn(Optional.of(v));

        ventaService.borrar(1);

        verify(ventaRepository).deleteById(1);
    }

    @Test
    public void testDescontarArticuloInventario()
    {
        // Como ya probamos el save tenemos que
        Venta v1 = new Venta();
        v1.setId(1);
        v1.setUsuarioId(1);
        v1.setVendedorId(1);
        v1.setSucursalId(1);
        v1.setCarritoId(1);
        v1.setListaProductos(new ArrayList<>());
        v1.setTotal(0);

        // Mockearemos las cosas
        Usuario u1 = new Usuario();
        u1.setId(1);

        Producto p1 = new Producto();
        p1.setId(1);

        Sucursal s1 = new Sucursal();
        s1.setId(1);
        s1.setInventarioId(1);
        Inventario inv1 = new Inventario();
        inv1.setId(1);
        inv1.setListaProductos(new ArrayList<>());
        inv1.getListaProductos().add(new ProductoInventario(1, 500));

        Carrito c1 = new Carrito();
        c1.setId(1);
        c1.setListaProductos(new ArrayList<>());
        c1.getListaProductos().add(new CarritoProducto(1, 100));

        when(restTemplate.getForObject(eq("http://localhost:8001/usuario/1"),eq(Usuario.class))).thenReturn(u1);
        when(restTemplate.getForObject(eq("http://localhost:8005/producto/1"),eq(Producto.class))).thenReturn(p1);
        when(restTemplate.getForObject(eq("http://localhost:8008/sucursal/1"),eq(Sucursal.class))).thenReturn(s1);
        when(restTemplate.getForObject(eq("http://localhost:8003/carrito/1"),eq(Carrito.class))).thenReturn(c1);
        when(restTemplate.getForObject(eq("http://localhost:8004/inventarios/1"),eq(Inventario.class))).thenReturn(inv1);

        //debemos simular que el repositorio guarda y retorna el usuario
        when(ventaRepository.save(v1)).thenReturn(v1);

        Venta resultado = ventaService.generarVenta(v1);

        // Debemos verificar si el inventario contiene menos items que antes
        // por los valores iniciales que dimos deberia ser asi 1 producto con 400 existencias
        assertEquals(1, inv1.getListaProductos().size());
        assertEquals(400, inv1.getListaProductos().get(0).getCantidad());
    }

    @Test
    public void testGetPorVendedorId() 
    {
        // Preparar datos de prueba
        List<CarritoProducto> listaProductos = new ArrayList<>();
        listaProductos.add(new CarritoProducto(1, 1));
        listaProductos.add(new CarritoProducto(2, 2));

        List<VentaProducto> listaProductosVenta = new ArrayList<>();
        listaProductosVenta.add(new VentaProducto(1, 1));
        listaProductosVenta.add(new VentaProducto(2, 2));
    
        // Venta 1 asociada al vendedor 1
        Venta v1 = new Venta();
        v1.setId(1);
        v1.setVendedorId(1);
        v1.setListaProductos(listaProductosVenta);
        
        // Venta 2 asociada al vendedor 2
        Venta v2 = new Venta();
        v2.setId(2);
        v2.setVendedorId(2);
        v2.setListaProductos(listaProductosVenta);
    
        when(ventaRepository.findByVendedorId(1)).thenReturn(Arrays.asList(v1));
    
        List<Venta> resultado = ventaService.getVentasPorVendedorId(1);
        
        // Solo una venta asociada al id 1!
        assertEquals(1, resultado.size());

        for (int i=0; i>resultado.size(); i++)
        {
            assertEquals(1, resultado.get(i).getVendedorId());
            assertEquals(i+1, resultado.get(i).getListaProductos().get(i).getProductoId());
            assertEquals(i+1, resultado.get(i).getListaProductos().get(i).getCantidad());
        }
    }
    
    
}
