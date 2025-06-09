package com.patitofeliz.sucursal_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.model.conexion.Alerta;
import com.patitofeliz.sucursal_service.repository.SucursalRepository;
import com.patitofeliz.sucursal_service.service.SucursalService;

public class SucursalServiceTest {
   @Mock
   private SucursalRepository sucursalRepository ;
   
   @InjectMocks
   private SucursalService sucursalService;


   @BeforeEach
   public void setup() {
      MockitoAnnotations.openMocks(this);
   }

   @Test
   public void TestGetAll(){
    Sucursal u1 = new Sucursal();
    u1.setId(1);
    u1.setGerenteId(1);
    u1.setNombreSucursal("Purfemelandia");
    Mockito.when(this.sucursalRepository.findAll()).thenReturn(Arrays.asList(u1));
    
    List<Sucursal> resultado = this.sucursalRepository.findAll();
    
    // Creamos la lista de objetos
    
    assertEquals(1, resultado.size());
    assertEquals("Purfemelandia",resultado.get(0).getNombreSucursal());

   }

   @Test
    public void testSucursalId_NoExiste(){
        when(sucursalRepository.findById(99)).thenReturn(Optional.empty());
        Sucursal resultado = sucursalService.listarSucursal(99);
        assertNull(resultado);
   }

   @Test
   public void testGetReview_Existe(){
       Sucursal r5 = new Sucursal();
       r5.setId(1);
       r5.setNombreSucursal("Perfumeschafa");

       when(sucursalRepository.findById(1)).thenReturn(Optional.of(r5));
       Sucursal resultado = sucursalService.listarSucursal(1);
       assertEquals(1, resultado.getId());
       assertEquals("Perfumeschafa", resultado.getNombreSucursal());
   }



   @Test
    public void testSave(){
        Sucursal u1 = new Sucursal();
        u1.setNombreSucursal("Purfumelandia");

        when(sucursalRepository.save(u1)).thenReturn(u1);
        Sucursal resultado = sucursalRepository.save(u1);
        assertEquals("Purfumelandia", resultado.getNombreSucursal());

    }

    @Test
    public void testBorrar(){
        Sucursal u1 = new Sucursal();
        u1.setId(1);

        when(sucursalRepository.findById(1)).thenReturn(Optional.of(u1));

        sucursalService.borrar(99);
        
        sucursalRepository.deleteById(1);
    }

}

