package com.patitofeliz.admin_service;

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
    public void TestGetAll(){
      Alerta u1  = new Alerta();  
      u1.setId(1);
      u1.setMensaje("alerta");
      u1.setTipoAlerta("carrito");
      u1.setFecha("02/06/2025");

      when(alertaRepository.findAll()).thenReturn(Arrays.asList(u1));


      List<Alerta>resultado = alertaSerivce.getAlertas();  
      
       assertEquals(1, resultado.size());
       assertEquals("alerta", resultado.get(0).getMensaje());

    }

    @Test
    public void testGetUsuarioById_NoExiste(){
        //simularemos que no existe un usuario con el id 99

        when(alertaRepository.findById(1)).thenReturn(Optional.empty());

        //ejecutamos el método
        Alerta resultado = alertaSerivce.getAlerta(1);

        //Verificamos el resultado
        assertNull(resultado);
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
    public void testSave(){
        //Se crea el usuario de prueba para guardar
        Alerta u = new Alerta();
        u.setMensaje("Carrito");

        //debemos simular que el repositorio guarda y retorna el usuario
        when(alertaRepository.save(u)).thenReturn(u);

        //ejecutamos el método
        Alerta resultado = alertaRepository.save(u);

        //Verificamos que el usuario guardado tenga el nombre correcto
        assertEquals("Carrito", resultado.getMensaje());
    } 
}
