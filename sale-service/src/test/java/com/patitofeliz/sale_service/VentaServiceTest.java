package com.patitofeliz.sale_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.patitofeliz.sale_service.model.Venta;
import com.patitofeliz.sale_service.model.CarritoProducto;
import com.patitofeliz.sale_service.repository.VentaRepository;
import com.patitofeliz.sale_service.service.VentaService;

public class VentaServiceTest 
{

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    public void setup() { MockitoAnnotations.openMocks(this); }

    @Test
    public void testGetAll(){
        // Lista de objetos
        CarritoProducto carritoVenta1 = new CarritoProducto();
        carritoVenta1.setProductoId(0);
        //Creamos un objeto de usuario
        Venta v1 = new Venta();
        v1.setId(1);
        v1.setCarritoId(1);
        v1.setListaProductos(null);

        //segundo objeto de usuario
        Venta v2 = new Venta();
        v2.setId(2);

        //Decimos que cuando se llame al metodo findAll()
        //retorne nuesta lista simulada
        when(ventaRepository.findAll()).thenReturn(Arrays.asList(v1,v2));

        //se ejecuta el método a testear
        List<Venta> resultado = ventaRepository.findAll();

        //Verificamos que el resultado sea el esperado. Para esto nos enfocaremos
        //en el tamaño de la lista y que los valores sean correctos.

        // Creamos la lista de objetos

        assertEquals(2, resultado.size());
        assertEquals("juanito", resultado.get(0).getListaProductos());
    }

}
