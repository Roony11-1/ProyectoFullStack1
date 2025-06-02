package com.patitofeliz.account_service;

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

import com.patitofeliz.account_service.model.Usuario;
import com.patitofeliz.account_service.repository.UsuarioRepository;
import com.patitofeliz.account_service.service.UsuarioService;

public class UsuarioServiceTest 
{

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setup() { MockitoAnnotations.openMocks(this); }

    @Test
    public void testGetAll(){
        //Creamos un objeto de usuario
        Usuario u1 = new Usuario();
        u1.setId(1);
        u1.setNombreUsuario("juanito");
        u1.setEmail("juanito@duoc.cl");
        u1.setTipoUsuario("admin");

        //segundo objeto de usuario
        Usuario u2 = new Usuario();
        u2.setId(2);
        u2.setNombreUsuario("megan");
        u2.setEmail("megancita@duoc.cl");
        u1.setTipoUsuario("vendedor");

        //Decimos que cuando se llame al metodo findAll()
        //retorne nuesta lista simulada
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1,u2));

        //se ejecuta el método a testear
        List<Usuario> resultado = usuarioService.getUsuarios();

        //Verificamos que el resultado sea el esperado. Para esto nos enfocaremos
        //en el tamaño de la lista y que los valores sean correctos.
        assertEquals(2, resultado.size());
        assertEquals("juanito", resultado.get(0).getNombreUsuario());


    }

    @Test
    public void testGetUsuarioById_NoExiste(){
        //simularemos que no existe un usuario con el id 99

        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        //ejecutamos el método
        Usuario resultado = usuarioService.getUsuario(99);

        //Verificamos el resultado
        assertNull(resultado);
    }

    @Test
    public void testSave(){
        //Se crea el usuario de prueba para guardar
        Usuario u = new Usuario();
        u.setNombreUsuario("Megan");

        //debemos simular que el repositorio guarda y retorna el usuario
        when(usuarioRepository.save(u)).thenReturn(u);

        //ejecutamos el método
        // Da error por el metodo de invocar alerta!! no es que este malo
        Usuario resultado = usuarioService.registrar(u);

        //Verificamos que el usuario guardado tenga el nombre correcto
        assertEquals("Megan", resultado.getNombreUsuario());
    }
}
