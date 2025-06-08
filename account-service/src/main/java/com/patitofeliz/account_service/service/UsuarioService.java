package com.patitofeliz.account_service.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.account_service.model.Usuario;
import com.patitofeliz.account_service.model.conexion.Alerta;
import com.patitofeliz.account_service.model.conexion.Carrito;
import com.patitofeliz.account_service.model.conexion.Review;
import com.patitofeliz.account_service.model.conexion.Venta;
import com.patitofeliz.account_service.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService 
{
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RestTemplate restTemplate;

    private static final String REVIEW_API = "http://localhost:8004/review";
    private static final String ALERTA_API = "http://localhost:8002/alerta";
    private static final String SALES_API = "http://localhost:8005";
    private static final String CARRITO_API = SALES_API+"/carrito";
    private static final String VENTA_API = SALES_API+"/venta";

    public List<Usuario> getUsuarios()
    {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuario(int id)
    {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario findByEmail(String email) 
    {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public boolean existePorId(int id) 
    {
        return usuarioRepository.existsById(id);
    }

    @Transactional
    public Usuario registrar(Usuario usuario)
    {
        // Verificación a nivel de software para evitar que nos ingresen email repetidos o nulos
        if (emailYaExiste(usuario.getEmail()))
            throw new IllegalArgumentException("Ya existe un usuario con ese email");

        Usuario nuevo = usuarioRepository.save(usuario);

        crearAlerta("Usuario registrado: "+nuevo.getNombreUsuario(), "Aviso: Usuario");

        return nuevo;
    }

    @Transactional
    public Usuario actualizar(int id, Usuario usuarioActualizado)
    {
        Usuario usuarioActual = usuarioRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        if (emailYaUsadoPorOtroUsuario(usuarioActualizado.getEmail(), id)) 
            throw new IllegalArgumentException("Ya existe un usuario con ese email");

        usuarioActual.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuarioActual.setEmail(usuarioActualizado.getEmail());
        usuarioActual.setPassword(usuarioActualizado.getPassword());
        usuarioActual.setTipoUsuario(usuarioActualizado.getTipoUsuario());

        return usuarioRepository.save(usuarioActual);
    }
    @Transactional
    public void borrar(int id)
    {
        usuarioRepository.deleteById(id);
    }

    // Metodos Auxiliares

    private boolean emailYaExiste(String email) 
    {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    private boolean emailYaUsadoPorOtroUsuario(String email, int idUsuario) 
    {
        Optional<Usuario> existente = usuarioRepository.findByEmail(email);

        // Verifica que no exista otro usuario con ese email, el dueño puede usarlo!!
        return existente.isPresent() && existente.get().getId() != idUsuario;
    }

    private void crearAlerta(String mensaje, String tipoAlerta)
    {
        Alerta alertaProductoRegistrado = new Alerta(mensaje, tipoAlerta);

        try
        {
            restTemplate.postForObject(ALERTA_API, alertaProductoRegistrado, Alerta.class);
        }
        catch (RestClientException e)
        {
            throw new IllegalArgumentException("No se pudo ingresar la Alerta: "+e);
        }
    }

    // Conexiones

    public List<Review> getReviewsByUsuarioId(int id) 
    {
        List<Review> listaReseñasPorId = restTemplate.getForObject(REVIEW_API+"/producto/"+id, List.class);

        if (listaReseñasPorId == null || listaReseñasPorId.isEmpty())
            throw new NoSuchElementException("No se encontraron reseñas para el usuario con ID: " + id);

        return listaReseñasPorId;
    }

    public List<Carrito> getCarritoByUsuarioId(int id)
    {
        List<Carrito> listaCarritoPorId = restTemplate.getForObject(CARRITO_API+"/usuario/"+id, List.class);

        if (listaCarritoPorId == null || listaCarritoPorId.isEmpty())
            throw new NoSuchElementException("No se encontraron carritos para el usuario con ID: " + id);

        return listaCarritoPorId;
    }

    public List<Venta> getVentasByUsuarioId(int id)
    {
        List<Venta> listaCarritoPorId = restTemplate.getForObject(VENTA_API+"/usuario/"+id, List.class);

        if (listaCarritoPorId == null || listaCarritoPorId.isEmpty())
            throw new NoSuchElementException("No se encontraron ventas para el usuario con ID: " + id);

        return listaCarritoPorId;
    }
}
