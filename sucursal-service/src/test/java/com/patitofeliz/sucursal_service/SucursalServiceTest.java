package com.patitofeliz.sucursal_service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.inventario.Inventario;
import com.patitofeliz.main.model.conexion.inventario.ProductoInventario;
import com.patitofeliz.main.model.conexion.producto.Producto;
import com.patitofeliz.main.model.conexion.usuario.Usuario;
import com.patitofeliz.main.model.dto.sucursal.SucursalInventarioDTO;
import com.patitofeliz.sucursal_service.model.Sucursal;
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
    public void TestGetAll()
    {
        // Crear sucursal
        Sucursal sucursal1 = new Sucursal();
        sucursal1.setId(1);
        sucursal1.setNombreSucursal("chupalo");
        sucursal1.setGerenteId(1);
        sucursal1.setInventarioId(1);
        sucursal1.setListaEmpleados(new ArrayList<>());

        // Crear sucursal
        Sucursal sucursal2 = new Sucursal();
        sucursal2.setId(2);
        sucursal2.setNombreSucursal("chupalo");
        sucursal2.setGerenteId(1);
        sucursal2.setInventarioId(1);
        sucursal2.setListaEmpleados(new ArrayList<>());
            
        // Mockear repositorios
        when(sucursalRepository.findAll()).thenReturn(Arrays.asList(sucursal1,sucursal2));

        List<Sucursal> resultado = sucursalService.getSucursales();

        assertEquals(2, resultado.size());

        for (int i=0; i>resultado.size(); i++)
        {
            assertEquals(i+1, resultado.get(i).getId());
            assertEquals("chupalo", resultado.get(i).getNombreSucursal());
            assertEquals(i+1, resultado.get(i).getGerenteId());
            assertEquals(1, resultado.get(i).getInventarioId());
            assertEquals(new ArrayList<>(), resultado.get(i).getListaEmpleados());
        }
    }

    @Test
    public void testSucursal_NoExiste() 
    {
        int idSucursal = 1;

        when(sucursalRepository.existsById(idSucursal)).thenReturn(false);

        boolean resultado = sucursalService.existePorId(idSucursal);

        assertTrue(!resultado);
    }

    @Test
    public void testSucursal_Existe() 
    {
        int idSucursal = 1;

        when(sucursalRepository.existsById(idSucursal)).thenReturn(true);

        boolean resultado = sucursalService.existePorId(idSucursal);

        assertTrue(resultado);
    }
   
    @Test
    public void testSave() 
    {
        Usuario gerente = new Usuario();
        gerente.setId(1);
        gerente.setTipoUsuario("gerente");
        when(restTemplate.getForObject(eq("http://localhost:8001/usuario/1"),eq(Usuario.class))).thenReturn(gerente);

        Inventario inventario = new Inventario();
        inventario.setId(1);
        inventario.setListaProductos(new ArrayList<>());
         when(restTemplate.postForObject(
        eq("http://localhost:8004/inventarios"),any(Inventario.class),eq(Inventario.class))).thenReturn(inventario);

        // Crear sucursal
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1);
        sucursal.setNombreSucursal("chupalo");
        sucursal.setGerenteId(1);
        sucursal.setInventarioId(1);
        sucursal.setListaEmpleados(new ArrayList<>());

        when(sucursalRepository.save(sucursal)).thenReturn(sucursal);

        Sucursal sucursalGuardada = sucursalService.guardar(sucursal);

        assertEquals(1, sucursalGuardada.getId());
        assertEquals("chupalo", sucursalGuardada.getNombreSucursal());
        assertEquals(1, sucursalGuardada.getGerenteId());
        assertEquals(1, sucursalGuardada.getInventarioId());
        assertEquals(new ArrayList<>(), sucursalGuardada.getListaEmpleados());
    }

    @Test
    public void testBorrar()
    {
        Sucursal u1 = new Sucursal();
        u1.setId(1);

        when(sucursalRepository.findById(1)).thenReturn(Optional.of(u1));

        sucursalService.borrar(99);
        
        sucursalRepository.deleteById(1);
    }

    @Test
    public void testAgregarProductosInventario() 
    {
        Usuario gerente = new Usuario();
        gerente.setId(1);
        gerente.setTipoUsuario("gerente");
        when(restTemplate.getForObject(eq("http://localhost:8001/usuario/1"), eq(Usuario.class)))
            .thenReturn(gerente);

        Producto producto = new Producto();
        producto.setId(1);
        when(restTemplate.getForObject(eq("http://localhost:8005/producto/1"), eq(Producto.class)))
            .thenReturn(producto);

        Inventario inventarioActualizado = new Inventario();
        inventarioActualizado.setId(1);
        inventarioActualizado.setListaProductos(List.of(new ProductoInventario(1, 10)));

        when(restTemplate.getForObject(eq("http://localhost:8004/inventarios/1"), eq(Inventario.class)))
            .thenReturn(inventarioActualizado);

        when(restTemplate.postForObject(
            eq("http://localhost:8004/inventarios/1/productos"),
            any(List.class),
            eq(Inventario.class))).thenReturn(null);

        // Mock de sucursal
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1);
        sucursal.setNombreSucursal("Sucursal Test");
        sucursal.setGerenteId(1);
        sucursal.setInventarioId(1);
        sucursal.setListaEmpleados(new ArrayList<>());

        when(sucursalRepository.findById(1)).thenReturn(Optional.of(sucursal));

        // Ejecutar
        SucursalInventarioDTO resultado = sucursalService.añadirProductosSucursal(1, List.of(new ProductoInventario(1, 10)));

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(1, resultado.getInventario().getListaProductos().size());
        assertEquals(1, resultado.getInventario().getListaProductos().get(0).getProductoId());
        assertEquals(10, resultado.getInventario().getListaProductos().get(0).getCantidad());
    }


}

