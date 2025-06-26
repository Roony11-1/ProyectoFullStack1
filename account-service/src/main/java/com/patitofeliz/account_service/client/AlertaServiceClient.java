package com.patitofeliz.account_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.account_service.model.conexion.alerta.Alerta;

@Service
public class AlertaServiceClient 
{

    private static final String ALERTA_API = "http://localhost:8002/alerta";

    @Autowired
    private RestTemplate restTemplate;

    public void crearAlerta(String mensaje, String tipoAlerta) 
    {
        try 
        {
            restTemplate.postForObject(ALERTA_API, new Alerta(mensaje, tipoAlerta), Alerta.class);
        } 
        catch (Exception e) 
        {
            throw new RestClientException("No se pudo ingresar la Alerta: " + e);
        }
    }

    public void crearAlertaSeguro(String mensaje, String tipo) 
    {
        try 
        {
            crearAlerta(mensaje, tipo);
        } 
        catch (RestClientException e) 
        {
            System.out.println("Error enviando alerta: " + e.getMessage());
        }
    }
}

