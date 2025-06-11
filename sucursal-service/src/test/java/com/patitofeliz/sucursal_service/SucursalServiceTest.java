package com.patitofeliz.sucursal_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.model.conexion.ProductoInventario;
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

  /*public Sucursal agregarProductosAInventario(int sucursalId, List<ProductoInventario> productos) {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new SucursalNotFoundException(sucursalId));
        
        inventarioServiceClient.agregarProductosASucursal(sucursalId, productos);
        return sucursalRepository.save(sucursal);/* */
    
    
   @Test
    public void TestGetAll(){
     Sucursal u1 = new Sucursal();
     u1.setId(1);
     u1.setGerenteId(1);
     u1.setNombreSucursal("Perfumelandia");
     Mockito.when(this.sucursalRepository.findAll()).thenReturn(Arrays.asList(u1));
    
     List<Sucursal> resultado = this.sucursalRepository.findAll();

    
     // Creamos la lista de objetos
    
        assertEquals(1, resultado.size());
        assertEquals("Perfumelandia", resultado.get(0).getNombreSucursal());
        assertEquals(1, resultado.get(0).getId());
        verify(sucursalRepository, times(1)).findAll();
   }

   @Test
    public void testSucursalId_NoExiste(){
        when(sucursalRepository.findById(99)).thenReturn(Optional.empty());
        Sucursal resultado = sucursalService.listarSucursal(99);
        assertNull(resultado);
        verify(sucursalRepository, times(1)).findById(99);
   }

   @Test
   public void testSucursal_Existe(){
       Sucursal r5 = new Sucursal();
       r5.setId(1);
       r5.setNombreSucursal("PerfumesChafa");

       when(sucursalRepository.findById(1)).thenReturn(Optional.of(r5));
       Sucursal resultado = sucursalService.listarSucursal(1);

       assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("PerfumesChafa", resultado.getNombreSucursal());
        verify(sucursalRepository, times(1)).findById(1);
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

