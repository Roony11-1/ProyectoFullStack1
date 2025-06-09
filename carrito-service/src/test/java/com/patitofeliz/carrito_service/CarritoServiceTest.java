package com.patitofeliz.carrito_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Array;
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

import com.patitofeliz.carrito_service.model.Carrito;
import com.patitofeliz.carrito_service.repository.RepositoryCarrito;
import com.patitofeliz.carrito_service.service.CarritoService;

public class CarritoServiceTest {
    @Mock
    private RepositoryCarrito carritoRepository;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CarritoService carritoService;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    ///test para obtener todos los carritos/// 
    @Test
    public void testGetCarritos(){
        Carrito c1=new Carrito();
        c1.setId(1);
        c1.setUsuarioId(1);

        Carrito c2=new Carrito();
        c2.setId(2);
        c2.setUsuarioId(2);

        when(carritoRepository.findAll()).thenReturn(Arrays.asList(c1,c2));
        List<Carrito> resultado=carritoService.getCarritos();
        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getId());
        assertEquals(2, resultado.get(1).getId());
    }
    ///test para obtener carrito por usuario id//
    @Test
    public void testGetCarritosByUsuarioId(){
        Carrito c1= new Carrito();
        c1.setId(1);
        c1.setUsuarioId(1);

        Carrito c2=new Carrito();
        c2.setId(2);
        c2.setUsuarioId(2);
        
        when(carritoRepository.findByUsuarioId(1)).thenReturn(Arrays.asList(c1,c2));

        List<Carrito> resultado=carritoService.getCarritosByUsuarioid(1);

        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getUsuarioId());
        assertEquals(1, resultado.get(1).getUsuarioId());
    }
    ///test para obtener carrito por id cuando no existe
    @Test
    public void testGetCarrito_NOExiste(){
        when(carritoRepository.findById(99)).thenReturn(Optional.empty());

        Carrito resultado=carritoService.getCarrito(99);

        assertNull(resultado);
    }
    ///test para obtener un carrito por id cuando existe
    @Test
    public void testGetCarrito_Existe(){
        Carrito c=new Carrito();
        c.setId(1);
        c.setUsuarioId(1);

        when(carritoRepository.findById(1)).thenReturn(Optional.of(c));

        Carrito resultado=carritoService.getCarrito(1);

        assertEquals(1, resultado.getId());
        assertEquals(1, resultado.getUsuarioId());
    }
    ///test guardar un carrito
    @Test
    public void testGuardar(){
        Carrito c = new Carrito();
        c.setId(1);

        when(carritoRepository.save(c)).thenReturn(c);

        Carrito resultado =carritoService.guardar(c);

        assertEquals(1, resultado.getId());
    }
    //test para actualizar carrito///
    @Test
    public void testActualizar(){
        Carrito c = new Carrito();
        c.setId(1);
        c.setUsuarioId(1);

        when(carritoRepository.findById(1)).thenReturn(Optional.of(c));

        Carrito resultado=carritoService.actualizar(1, c);

        assertEquals(2,resultado.getUsuarioId());
    }
    //test eliminar un carrito//
    @Test
    public void testBorrar(){
        Carrito c=new Carrito();
        c.setId(1);

        when(carritoRepository.findById(1)).thenReturn(Optional.of(c));

        carritoService.borrar(1);

        verify(carritoRepository).deleteById(1);
    }

}
