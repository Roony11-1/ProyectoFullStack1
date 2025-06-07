package com.patitofeliz.account_service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.account_service.model.Usuario;
import com.patitofeliz.account_service.repository.UsuarioRepository;
import com.patitofeliz.account_service.service.UsuarioService;

public class UsuarioServiceTest 
{

    @Mock
    private UsuarioRepository usuarioRepository;

    // Simulamos el rest para el metodo crearAlerta
    @Mock
    private RestTemplate restTemplate;

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
        u2.setTipoUsuario("vendedor");

        //Decimos que cuando se llame al metodo findAll()
        //retorne nuesta lista simulada
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1,u2));

        //se ejecuta el método a testear
        List<Usuario> resultado = usuarioService.getUsuarios();

        //Verificamos que el resultado sea el esperado. Para esto nos enfocaremos
        //en el tamaño de la lista y que los valores sean correctos.
        // Tamaño de la lista
        assertEquals(2, resultado.size());

        // Verificamos el objeto usuario
        assertEquals("juanito", resultado.get(0).getNombreUsuario());
        assertEquals("juanito@duoc.cl", resultado.get(0).getEmail());
        assertEquals("admin", resultado.get(0).getTipoUsuario());

        // Verificamos el segundo usuario        
        assertEquals("megan", resultado.get(1).getNombreUsuario());
        assertEquals("megancita@duoc.cl", resultado.get(1).getEmail());
        assertEquals("vendedor", resultado.get(1).getTipoUsuario());
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
    public void testRegistrar(){
        //Se crea el usuario de prueba para guardar
        Usuario u = new Usuario();
        u.setNombreUsuario("Megan");
        u.setEmail("megan@duoc.cl");
        u.setTipoUsuario("admin");

        //debemos simular que el repositorio guarda y retorna el usuario
        when(usuarioRepository.save(u)).thenReturn(u);

        //ejecutamos el método
        // Da error por el metodo de invocar alerta!! no es que este malo
        Usuario resultado = usuarioService.registrar(u);

        //Verificamos que el usuario guardado tenga el nombre correcto
        assertEquals("Megan", resultado.getNombreUsuario());
        assertEquals("megan@duoc.cl", resultado.getEmail());
        assertEquals("admin", resultado.getTipoUsuario());
    }

    @Test
    public void testBorrarUsuario() 
    {
        Usuario u = new Usuario();
        u.setId(1);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));

        usuarioService.borrar(1);

        verify(usuarioRepository).deleteById(1);
    }

    @Test
    public void testRegistrar_EmailYaExiste_LanzaExcepcion() 
    {
        Usuario existente = new Usuario();
        existente.setId(1);
        existente.setEmail("repetido@duoc.cl");

        Usuario nuevo = new Usuario();
        nuevo.setEmail("repetido@duoc.cl");

        when(usuarioRepository.findByEmail("repetido@duoc.cl")).thenReturn(Optional.of(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrar(nuevo);
        });

        assertEquals("Ya existe un usuario con ese email", ex.getMessage());
    }

    @Test
    public void testActualizar_EmailEnUsoPorOtroUsuario_LanzaExcepcion() 
    {
        Usuario existente = new Usuario();
        existente.setId(1);
        existente.setEmail("original@duoc.cl");

        Usuario otro = new Usuario();
        otro.setId(2);
        otro.setEmail("repetido@duoc.cl");

        Usuario actualizado = new Usuario();
        actualizado.setEmail("repetido@duoc.cl");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmail("repetido@duoc.cl")).thenReturn(Optional.of(otro));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.actualizar(1, actualizado);
        });

        assertEquals("Ya existe un usuario con ese email", ex.getMessage());
    }

    @Test
    public void testFindByEmail_NoExiste() 
    {
        when(usuarioRepository.findByEmail("nada@duoc.cl")).thenReturn(Optional.empty());

        Usuario resultado = usuarioService.findByEmail("nada@duoc.cl");

        assertNull(resultado);
    }
}
