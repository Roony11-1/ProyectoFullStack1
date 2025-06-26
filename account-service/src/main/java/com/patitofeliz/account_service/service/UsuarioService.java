package com.patitofeliz.account_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.account_service.model.Usuario;
import com.patitofeliz.account_service.repository.UsuarioRepository;
import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.CarritoServiceClient;
import com.patitofeliz.main.client.ReviewServiceClient;
import com.patitofeliz.main.client.VentaServiceClient;
import com.patitofeliz.main.model.conexion.carrito.Carrito;
import com.patitofeliz.main.model.conexion.review.Review;
import com.patitofeliz.main.model.conexion.venta.Venta;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService 
{
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ReviewServiceClient reviewServiceClient;
    @Autowired
    private AlertaServiceClient alertaServiceClient;
    @Autowired
    private CarritoServiceClient carritoServiceClient;
    @Autowired
    private VentaServiceClient ventaServiceClient;

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

        alertaServiceClient.crearAlertaSeguro("Usuario registrado: " + nuevo.getNombreUsuario(), "Aviso: Usuario");

        return nuevo;
    }

    public List<Usuario> registrarLote(List<Usuario> usuarios)
    {
        List<Usuario> listaRegistrados = new ArrayList<>();

        for (Usuario usuario : usuarios) 
        {
            try 
            {
                Usuario registrado = registrar(usuario);
                listaRegistrados.add(registrado);
            } 
            catch (IllegalArgumentException e) 
            {
                System.out.println("Usuario omitido: " + usuario.getEmail() + " -> " + e.getMessage());
            }
        }
        return listaRegistrados;
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

        alertaServiceClient.crearAlertaSeguro("Usuario Actualizado ID: "+usuarioActual.getId(), "Aviso: Usuario");

        return usuarioRepository.save(usuarioActual);
    }
    @Transactional
    public void borrar(int id)
    {
        alertaServiceClient.crearAlertaSeguro("Usuario Eliminado ID: "+getUsuario(id).getId(), "Aviso: Usuario");
        usuarioRepository.deleteById(id);
    }

    public List<Review> getReviewsByUsuarioId(int usuarioId)
    {
        List<Review> listaReviews = reviewServiceClient.getReviewsByUsuarioId(usuarioId);

        return listaReviews;
    }

    public List<Carrito> getCarritoByUsuarioId(int id)
    {
        List<Carrito> listaCarritos = carritoServiceClient.getCarritoByUsuarioId(id);

        return listaCarritos;
    }

    public List<Venta> getVentasByUsuarioId(int id)
    {
        List<Venta> listaVentas = ventaServiceClient.getVentasByUsuarioId(id);

        return listaVentas;
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
}
