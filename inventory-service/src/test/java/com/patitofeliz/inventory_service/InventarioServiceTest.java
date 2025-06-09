package com.patitofeliz.inventory_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.inventory_service.model.Inventario;
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
    ///test para obtenet inventario///
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


}
