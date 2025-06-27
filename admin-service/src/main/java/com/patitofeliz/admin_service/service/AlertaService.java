package com.patitofeliz.admin_service.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.admin_service.model.Alerta;
import com.patitofeliz.admin_service.repository.AlertaRepository;

import jakarta.transaction.Transactional;

@Service
public class AlertaService 
{
    @Autowired
    private AlertaRepository alertaRepository;
    @Autowired

    public List<Alerta> getAlertas()
    {
        return alertaRepository.findAll();
    }

    public Alerta getAlerta(int id)
    {
        return alertaRepository.findById(id).orElse(null);
    }

    public List<Alerta> getAlertaByTipoAlerta(String tipoAlerta)
    {
        return alertaRepository.findByTipoAlertaContainingIgnoreCase(tipoAlerta);
    }

    public boolean existePorId(int id) 
    {
        return alertaRepository.existsById(id);
    }

    @Transactional
    public Alerta guardar(Alerta alerta)
    {
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        alerta.setFecha(fechaActual);

        Alerta alertaNueva = alertaRepository.save(alerta);

        return alertaNueva;
    }

    @Transactional
    public Alerta actualizar(int id, Alerta alertaActualizada)
    {
        Alerta alertaActual = alertaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Alerta no encontrada"));

        alertaActual.setMensaje(alertaActualizada.getMensaje());
        alertaActual.setTipoAlerta(alertaActualizada.getTipoAlerta());
        alertaActual.setFecha(alertaActualizada.getFecha());

        Alerta alertaEliminacion = new Alerta("Se actualizó la alerta con ID: " + id, "Alerta");
        alertaRepository.save(alertaEliminacion);

        return alertaRepository.save(alertaActual);
    }

    @Transactional
    public void borrar(int id)
    {   
        if (!existePorId(id))
            throw new NoSuchElementException("No se encontró la alerta con ID: " + id);

        alertaRepository.deleteById(id);

        Alerta alertaEliminacion = new Alerta("Se eliminó la alerta con ID: " + id, "Alerta");
        alertaRepository.save(alertaEliminacion);
    }
}
