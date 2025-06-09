package com.patitofeliz.sale_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sale_service.model.Venta;
import com.patitofeliz.sale_service.model.CarritoProducto;
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
        List<CarritoProducto> listaProductos = new ArrayList<>();
        listaProductos.add(new CarritoProducto(1, 1));
        listaProductos.add(new CarritoProducto(2, 2));
        listaProductos.add(new CarritoProducto(3, 3));
        listaProductos.add(new CarritoProducto(4, 4));

        // Venta 1
        Venta v1 = new Venta();
        v1.setId(1);
        v1.setCarritoId(1);
        v1.setListaProductos(listaProductos);

        // Lista de productos 2
        List<CarritoProducto> listaProductos2 = new ArrayList<>();
        listaProductos2.add(new CarritoProducto(1, 1));
        listaProductos2.add(new CarritoProducto(2, 2));
        listaProductos2.add(new CarritoProducto(3, 3));
        listaProductos2.add(new CarritoProducto(4, 4));

        // Venta 2
        Venta v2 = new Venta();
        v2.setId(2);
        v2.setCarritoId(2);
        v2.setListaProductos(listaProductos2);

        when(ventaRepository.findAll()).thenReturn(Arrays.asList(v1,v2));

        List<Venta> resultado = ventaRepository.findAll();

        // Verificamos el largo
        assertEquals(2, resultado.size());
        // Verificamos el Id
        assertEquals(1, resultado.get(0).getId());
        // Verificamos el CarritoId
        assertEquals(1, resultado.get(0).getCarritoId());
        // Verificamos un objeto cualquiera
        assertEquals(2, resultado.get(0).getListaProductos().get(1).getProductoId());

        // Verificamos el Id
        assertEquals(2, resultado.get(1).getId());
        // Verificamos el CarritoId
        assertEquals(2, resultado.get(1).getCarritoId());

        // Dejamos los objetos con datos de patron simple para comprobar cada uno
        for (Integer i = 0; i < listaProductos.size(); i++) 
        {
            assertEquals(i+1, v1.getListaProductos().get(i).getProductoId());
            assertEquals(i+1, v1.getListaProductos().get(i).getCantidad());
        }

        // Dejamos los objetos con datos de patron simple para comprobar cada uno
        for (Integer i = 0; i < listaProductos2.size(); i++) 
        {
            assertEquals(i+1, v2.getListaProductos().get(i).getProductoId());
            assertEquals(i+1, v2.getListaProductos().get(i).getCantidad());
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
        assertEquals(new ArrayList<>(), resultado.getListaProductos());
        assertEquals(0, resultado.getTotal());
    }

    @Test
    public void testBorrarVenta() 
    {
        Venta v = new Venta();
        v.setId(1);

        when(ventaRepository.findById(1)).thenReturn(Optional.of(v));

        ventaService.elimiarPorId(1);

        verify(ventaRepository).deleteById(1);
    }
}
