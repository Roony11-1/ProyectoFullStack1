package com.patitofeliz.admin_service.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.admin_service.model.Alerta;
import com.patitofeliz.admin_service.repository.AlertaRepository;

@Service
public class AlertaService 
{
    @Autowired
    private AlertaRepository alertaRepository;

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

    public Alerta guardar(Alerta alerta)
    {
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        alerta.setFecha(fechaActual);

        Alerta alertaNueva = alertaRepository.save(alerta);

        return alertaNueva;
    }

    public Alerta actualizar(int id, Alerta alertaActualizada)
    {
        Alerta alertaActual = alertaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Alerta no encontrada"));

        alertaActual.setMensaje(alertaActualizada.getMensaje());
        alertaActual.setTipoAlerta(alertaActualizada.getTipoAlerta());
        alertaActual.setFecha(alertaActualizada.getFecha());

        return alertaRepository.save(alertaActual);
    }

    public void borrar(int id)
    {
        alertaRepository.deleteById(id);
    }
}
