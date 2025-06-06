package com.patitofeliz.carrito_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        MockitoAnnotations.openMocks(carritoRepository);
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

}
