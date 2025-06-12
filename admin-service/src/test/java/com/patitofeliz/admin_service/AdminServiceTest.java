package com.patitofeliz.admin_service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.patitofeliz.admin_service.model.Alerta;
import com.patitofeliz.admin_service.repository.AlertaRepository;
import com.patitofeliz.admin_service.service.AlertaService;

public class AdminServiceTest {
    @Mock
	private AlertaRepository alertaRepository;

	@InjectMocks 
	private AlertaService alertaSerivce;

    @BeforeEach
     public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void TestGetAll()
    {
        Alerta u1  = new Alerta();  
        u1.setId(1);
        u1.setMensaje("alerta");
        u1.setTipoAlerta("carrito");
        u1.setFecha("02/06/2025");

        Alerta u2  = new Alerta();  
        u2.setId(2);
        u2.setMensaje("alerta");
        u2.setTipoAlerta("carrito");
        u2.setFecha("02/06/2025");

        when(alertaRepository.findAll()).thenReturn(Arrays.asList(u1, u2));


        List<Alerta>resultado = alertaSerivce.getAlertas();  
      
        assertEquals(2, resultado.size());
       
        assertEquals(1, resultado.get(0).getId());
        assertEquals("alerta", resultado.get(0).getMensaje());
        assertEquals("carrito", resultado.get(0).getTipoAlerta());
        assertEquals("02/06/2025", resultado.get(0).getFecha());

        assertEquals(2, resultado.get(1).getId());
        assertEquals("alerta", resultado.get(1).getMensaje());
        assertEquals("carrito", resultado.get(1).getTipoAlerta());
        assertEquals("02/06/2025", resultado.get(1).getFecha());
    }


    @Test
    public void testGetAlertaById_NoExiste(){
        //simularemos que no existe un usuario con el id 99

        when(alertaRepository.findById(99)).thenReturn(Optional.empty());

        //ejecutamos el método
        Alerta resultado = alertaSerivce.getAlerta(1);

        //Verificamos el resultado
        assertNull(resultado);
    }

    @Test
    public void testSave()
    {
        //Se crea el usuario de prueba para guardar
        Alerta u = new Alerta();
        u.setId(1);
        u.setMensaje("alerta");
        u.setTipoAlerta("carrito");
        u.setFecha("02/06/2025");

        //debemos simular que el repositorio guarda y retorna el usuario
        when(alertaRepository.save(u)).thenReturn(u);

        //ejecutamos el método
        Alerta resultado = alertaRepository.save(u);

        //Verificamos que el usuario guardado tenga el nombre correcto
        assertEquals(1, resultado.getId());
        assertEquals("alerta", resultado.getMensaje());
        assertEquals("carrito", resultado.getTipoAlerta());
        assertEquals("02/06/2025", resultado.getFecha());
    } 

    @Test
    public void testBorrarAlerta() 
    {
        Alerta a = new Alerta();
        a.setId(1);

        when(alertaRepository.findById(1)).thenReturn(Optional.of(a));

        alertaSerivce.borrar(1);

        verify(alertaRepository).deleteById(1);
    }
}
