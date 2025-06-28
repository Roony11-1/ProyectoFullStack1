package com.patitofeliz.main.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.alerta.Alerta;


@Service
public class AlertaServiceClient 
{

    private static final String ALERTA_API = "http://localhost:8002/alerta";

    @Autowired
    private RestTemplate restTemplate;

    public void crearAlerta(String mensaje, String tipoAlerta) 
    {
        restTemplate.postForObject(ALERTA_API, new Alerta(mensaje, "Aviso: "+tipoAlerta), Alerta.class);
    }
}

