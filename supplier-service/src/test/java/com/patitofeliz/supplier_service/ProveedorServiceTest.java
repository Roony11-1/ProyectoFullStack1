package com.patitofeliz.supplier_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.supplier_service.model.Proveedor;
import com.patitofeliz.supplier_service.repository.ProveedorRepository;
import com.patitofeliz.supplier_service.service.ProveedorService;

public class ProveedorServiceTest 
{
    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private AlertaServiceClient alertaServiceClient;

    @InjectMocks
    private ProveedorService proveedorService;

    @BeforeEach
    public void setup() 
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProveedores() 
    {
        Proveedor p1 = new Proveedor(1, "Proveedor A");
        Proveedor p2 = new Proveedor(2, "Proveedor B");

        when(proveedorRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Proveedor> resultado = proveedorService.getProveedores();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(p1));
        assertTrue(resultado.contains(p2));
    }

    @Test
    public void testGetProveedor_found() 
    {
        Proveedor p = new Proveedor(1, "Proveedor A");

        when(proveedorRepository.findById(1)).thenReturn(Optional.of(p));

        Proveedor resultado = proveedorService.getProveedor(1);

        assertEquals(p, resultado);
    }

    @Test
    public void testGetProveedor_notFound() 
    {
        when(proveedorRepository.findById(999)).thenReturn(Optional.empty());

        Proveedor resultado = proveedorService.getProveedor(999);

        assertNull(resultado);
    }

    @Test
    public void testExistePorId_true() 
    {
        when(proveedorRepository.existsById(1)).thenReturn(true);

        assertTrue(proveedorService.existePorId(1));
    }

    @Test
    public void testExistePorId_false() 
    {
        when(proveedorRepository.existsById(999)).thenReturn(false);

        assertFalse(proveedorService.existePorId(999));
    }

    @Test
    public void testGuardar() 
    {
        Proveedor p = new Proveedor(1, "Proveedor A");

        when(proveedorRepository.save(p)).thenReturn(p);
        doNothing().when(alertaServiceClient).crearAlerta(anyString(), anyString());

        Proveedor resultado = proveedorService.guardar(p);

        assertEquals(p, resultado);
        verify(alertaServiceClient, times(1)).crearAlerta(contains("Proveedor registrado ID"), eq("Proveedor"));
    }

    @Test
    public void testGuardarLote() 
    {
        Proveedor p1 = new Proveedor(1, "Proveedor A");
        Proveedor p2 = new Proveedor(2, "Proveedor B");

        when(proveedorRepository.save(p1)).thenReturn(p1);
        when(proveedorRepository.save(p2)).thenReturn(p2);

        List<Proveedor> resultado = proveedorService.guardarLote(Arrays.asList(p1, p2));

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(p1));
        assertTrue(resultado.contains(p2));
    }

    @Test
    public void testBorrar_existente() 
    {
        when(proveedorRepository.existsById(1)).thenReturn(true);
        doNothing().when(proveedorRepository).deleteById(1);
        doNothing().when(alertaServiceClient).crearAlerta(anyString(), anyString());

        assertDoesNotThrow(() -> proveedorService.borrar(1));
        verify(proveedorRepository, times(1)).deleteById(1);
        verify(alertaServiceClient, times(1)).crearAlerta(contains("Proveedor borrado - ID"), eq("Proveedor"));
    }

    @Test
    public void testBorrar_noExistente() 
    {
        when(proveedorRepository.existsById(999)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> proveedorService.borrar(999));
    }

    @Test
    public void testActualizaProveedor() 
    {
        Proveedor pActual = new Proveedor(1, "Proveedor A");
        Proveedor pActualizado = new Proveedor(1, "Proveedor A actualizado");

        when(proveedorRepository.findById(1)).thenReturn(Optional.of(pActual));
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(alertaServiceClient).crearAlerta(anyString(), anyString());

        Proveedor resultado = proveedorService.actualizaProveedor(1, pActualizado);

        assertNotNull(resultado);
        verify(alertaServiceClient).crearAlerta(contains("Usuario Actualizado ID"), eq("Proveedor"));
    }
}
