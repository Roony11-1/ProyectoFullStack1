package com.patitofeliz.inventory_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.inventory_service.model.Inventario;
import com.patitofeliz.inventory_service.model.ProductoInventario;
import com.patitofeliz.inventory_service.model.conexion.Alerta;
import com.patitofeliz.inventory_service.repository.InventarioRepository;
import com.patitofeliz.inventory_service.service.InventarioService;

public class InventarioServiceTest {
    @InjectMocks
    private InventarioService inventarioService;
    @Mock
    private InventarioRepository inventarioRepository;
    @Mock
    private RestTemplate restTemplate;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    ///test para obtener inventario///
    @Test
    public void testGetInventario(){
        Inventario inventario=new Inventario();
        inventario.setId(1);
        
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.getInventario(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }
    ///test para guardar inventario///
    @Test
    public void testGuardarInventario(){
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setId(1);

        when(inventarioRepository.save(any(Inventario.class))).thenReturn(nuevoInventario);

        Inventario resultado = inventarioService.guardarInventario(nuevoInventario);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(restTemplate).postForObject(eq("http://localhost:8002/alerta"), any(Alerta.class),eq(Alerta.class));
    }
    ///test vaciar inventario///
    @Test
    public void testVaciarInventario(){
        Inventario inventario=new Inventario();
        inventario.setId(1);
        inventario.setListaProductos(List.of(new ProductoInventario()));

        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario resultado = inventarioService.vaciarInventario(1);

        assertTrue(resultado.getListaProductos().isEmpty());
    }
    ///test eliminar producto inventario///
    @Test
    public void TestEliminarProductoInventario(){
        ProductoInventario producto= new ProductoInventario();
        producto.setProductoId(101);
        producto.setCantidad(10);
        List<ProductoInventario> productos = new ArrayList<>(List.of(producto));

        Inventario inventario = new Inventario();
        inventario.setId(1);
        inventario.setListaProductos(new ArrayList<>(List.of(producto)));
        

        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario resultado= inventarioService.eliminarProductoInventario(1, productos);

        assertNotNull(resultado);
        assertEquals(0, resultado.getListaProductos().size());
        verify(restTemplate).postForObject(eq("http://localhost:8002/alerta"),any(Alerta.class),eq(Alerta.class));
    
    }
    /// test existe por id///
    @Test
    public void testExistePorId_CuandoExiste(){
        when(inventarioRepository.existsById(1)).thenReturn(true);

        boolean resultado=inventarioService.existePorId(1);
        assertTrue(resultado);
    }
    ///test cuando no existe///
    @Test
    public void testExistePorId_CuandoNoExiste(){
        when(inventarioRepository.existsById(2)).thenReturn(false);

        boolean resultado=inventarioService.existePorId(2);
        assertFalse(resultado);
    }
    


}
