package com.patitofeliz.producto_service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.patitofeliz.producto_service.model.Producto;
import com.patitofeliz.producto_service.repository.ProductoRepository;
import com.patitofeliz.producto_service.service.ProductoService;


public class ProductoServiceTest 
{
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll()
    {
        //Creamos un objeto Producto
        Producto p1 = new Producto();
        p1.setId(1);
        p1.setNombre("Iphone 13");
        p1.setMarca("Apple");
        p1.setPrecio(1000000);

        //Creamos segundo Producto
        Producto p2 = new Producto();
        p2.setId(2);
        p2.setNombre("Iphone 16");
        p2.setMarca("Apple");
        p2.setPrecio(2000000);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1,p2));

        List<Producto> resultado = productoService.getProductos();

        assertEquals(2, resultado.size());
        assertEquals("Iphone 13", resultado.get(0).getNombre());
    }

    @Test
    public void testGetProductoById_NoExiste()
    {
        // Simularemos que no existe un producto con el id 100
        when(productoRepository.findById(100)).thenReturn(Optional.empty());
        //ejecutamos el metodo
        Producto resultado = productoService.getProducto(100);

        // Verificamos el resultado
        assertNull(resultado);
    }

    @Test
    public void testSave()
    {
        // Se crea el objeto producto de prueba para guardarlo
        Producto p = new Producto();
        p.setNombre("Iphone 13");
        p.setMarca("Apple");
        p.setPrecio(500000);

        when(productoRepository.save(p)).thenReturn(p);

        Producto resultado = productoService.registrar(p);

        // Verificamos que el usuario guardado tenga el nombre correcto
        assertEquals("Iphone 13", resultado.getNombre());
        assertEquals("Apple", resultado.getMarca());
        assertEquals(500000, resultado.getPrecio());
    }

    @Test
    public void testActualizar()
    {
        Producto p = new Producto();
        p.setId(1);
        p.setNombre("Iphone 13");
        p.setMarca("Apple");
        p.setPrecio(1000000);

        Producto a = new Producto();
        a.setId(1);
        a.setNombre("Iphone 12");
        a.setMarca("Pineaple");
        a.setPrecio(1);

        when(productoRepository.findById(1)).thenReturn(Optional.of(p));
        when(productoRepository.save(any(Producto.class))).thenReturn(p);

        Producto resultado = productoService.actualizar(1, a);

        assertEquals(1, resultado.getId());
        assertEquals("Iphone 12", resultado.getNombre());
        assertEquals("Pineaple", resultado.getMarca());
        assertEquals(1, resultado.getPrecio());
    }

    @Test
    public void testEliminar()
    {
        Producto p = new Producto();
        p.setId(1);

        when(productoRepository.findById(1)).thenReturn(Optional.of(p));

        productoService.borrar(1);

        verify(productoRepository).deleteById(1);
    }
}
