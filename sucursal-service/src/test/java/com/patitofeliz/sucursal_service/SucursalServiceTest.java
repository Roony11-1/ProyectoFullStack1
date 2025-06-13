package com.patitofeliz.sucursal_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.model.conexion.Carrito;
import com.patitofeliz.sucursal_service.model.conexion.CarritoProducto;
import com.patitofeliz.sucursal_service.model.conexion.Inventario;
import com.patitofeliz.sucursal_service.model.conexion.Producto;
import com.patitofeliz.sucursal_service.model.conexion.ProductoInventario;
import com.patitofeliz.sucursal_service.model.conexion.Usuario;
import com.patitofeliz.sucursal_service.model.conexion.Venta;
import com.patitofeliz.sucursal_service.repository.SucursalRepository;
import com.patitofeliz.sucursal_service.service.SucursalService;

public class SucursalServiceTest {
   @Mock
   private SucursalRepository sucursalRepository ;
   
   @InjectMocks
   private SucursalService sucursalService;
      
    @Mock
    private RestTemplate restTemplate;

   @BeforeEach
   public void setup() {
      MockitoAnnotations.openMocks(this);
   }

    
    
   @Test
    public void TestGetAll(){
   
        int sucursalId = 1;
        int inventarioId = 100;
        
        // Crear sucursal
        Sucursal sucursal = new Sucursal();
        sucursal.setId(sucursalId);
        sucursal.setInventarioId(inventarioId);
        
        // Crear inventario existente
        Inventario inventario = new Inventario();
        inventario.setId(inventarioId);
        inventario.setListaProductos(new ArrayList<>());
        
          // Producto existente en inventario
        ProductoInventario productoExistente = new ProductoInventario(101, 500);
        inventario.getListaProductos().add(productoExistente);

        // Nuevos productos a agregar
        ProductoInventario nuevoProducto1 = new ProductoInventario(102, 50);
        ProductoInventario nuevoProducto2 = new ProductoInventario(103, 30);
        List<ProductoInventario> nuevosProductos = List.of(nuevoProducto1, nuevoProducto1);
         
        // Mockear repositorios
        when(sucursalRepository.findAll()).thenReturn(Arrays.asList(1));
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(1));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(invocation -> invocation.getArgument(0));


         // Ejecutar el método
        Inventario resultado = sucursalService.agregarProductosAInventario(sucursalId, nuevosProductos);

        
        // Verificaciones
        assertNotNull(resultado);
        assertEquals(3, resultado.getListaProductos().size()); // 1 existente + 2 nuevos    
   }

   @Test
    public void testSucursalId_NoExiste(){
        when(sucursalRepository.findById(99)).thenReturn(Optional.empty());
        Sucursal resultado = sucursalService.listarSucursal(99);
        assertNull(resultado);
        verify(sucursalRepository, times(1)).findById(99);
   }

   @Test
   public void testSucursal_Existe(){
        int sucursalId = 1;
        int inventarioId = 100;
        int productoId = 101;
        int cantidadVenta = 10;
        
        // Mockear objetos necesarios
        Usuario usuario = new Usuario();
        usuario.setId(1);
        
        Producto producto = new Producto();
        producto.setId(productoId);
        producto.setPrecio(2599);
        
        Sucursal sucursal = new Sucursal();
        sucursal.setId(sucursalId);
        sucursal.setInventarioId(inventarioId);
        
        Inventario inventario = new Inventario();
        inventario.setId(inventarioId);
        inventario.setListaProductos(new ArrayList<>());
        
        // Agregar producto al inventario
        ProductoInventario productoInventario = new ProductoInventario(productoId, 100);
        inventario.getListaProductos().add(productoInventario);
        
        // Crear carrito
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setListaProductos(new ArrayList<>());
        
        // Agregar producto al carrito
        CarritoProducto carritoProducto = new CarritoProducto(productoId, cantidadVenta);
        carrito.getListaProductos().add(carritoProducto);
        
        // Crear venta
        Venta venta = new Venta();
        venta.setId(1);
        venta.setUsuarioId(usuario.getId());
        venta.setSucursalId(sucursalId);
        venta.setCarritoId(carrito.getId());
        venta.setListaProductos(carrito.getListaProductos());

        // Mockear llamadas a otros servicios
        when(restTemplate.getForObject("http://usuario-service/usuario/" + usuario.getId(), Usuario.class))
            .thenReturn(usuario);
        
        when(restTemplate.getForObject("http://producto-service/producto/" + productoId, Producto.class))
            .thenReturn(producto);
        
        when(restTemplate.getForObject("http://sucursal-service/sucursal/" + sucursalId, Sucursal.class))
            .thenReturn(sucursal);
        
        when(restTemplate.getForObject("http://carrito-service/carrito/" + carrito.getId(), Carrito.class))
            .thenReturn(carrito);
        
        when(restTemplate.getForObject("http://inventario-service/inventarios/" + inventarioId, Inventario.class))
            .thenReturn(inventario);
        
        // Mockear repositorios
        when(sucursalRepository.findById(sucursalId)).thenReturn(Optional.of(sucursal));
        when(inventarioRepository.findById(inventarioId)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar el método
        Venta resultado = sucursalService.procesarVentaSucursal(venta);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        
        // Verificar que el inventario fue actualizado
        assertEquals(90, inventario.getListaProductos().get(0).getCantidad());
        
        // Verificar que el total de la venta se calculó correctamente
        double totalEsperado = cantidadVenta * producto.getPrecio();
        assertEquals(totalEsperado, resultado.getTotal(), 0.001);
        
        // Verificar interacciones
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
        verify(restTemplate, times(5)).getForObject(anyString(), any());
    }
   



   @Test
    public void testSave(){
        Sucursal u1 = new Sucursal();
        u1.setNombreSucursal("Purfumelandia");

        when(sucursalRepository.save(u1)).thenReturn(u1);
        Sucursal resultado = sucursalRepository.save(u1);
        assertEquals("Purfumelandia", resultado.getNombreSucursal());

    }

    @Test
    public void testBorrar(){
        Sucursal u1 = new Sucursal();
        u1.setId(1);

        when(sucursalRepository.findById(1)).thenReturn(Optional.of(u1));

        sucursalService.borrar(99);
        
        sucursalRepository.deleteById(1);
    }

}

