package com.patitofeliz.main.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.proveedor.Pedido;

@Service
public class PedidosServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String PEDIDOS_API = "http://localhost:8009/pedidos";

    public Pedido getProveedor(int pedidoId) 
    {
        Pedido pedido = restTemplate.getForObject(PEDIDOS_API + "/" + pedidoId, Pedido.class);

        if (pedido == null)
            throw new NoSuchElementException("Proveedor no encontrado con ID: " + pedidoId);

        return pedido;
    }

    public Pedido crearPedido(Pedido pedido) 
    {
        Pedido pedidoCreado = restTemplate.postForObject(PEDIDOS_API, pedido, Pedido.class);

        return pedidoCreado;
    }
}
