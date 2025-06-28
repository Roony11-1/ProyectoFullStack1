package com.patitofeliz.supplier_service.controller;

import com.patitofeliz.supplier_service.model.Pedido;
import com.patitofeliz.supplier_service.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController 
{

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> getPedidos() 
    {
        List<Pedido> pedidos = pedidoService.getPedidos();

        if (pedidos.isEmpty())
            return ResponseEntity.noContent().build();
        
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedido(@PathVariable int id) 
    {
        Pedido pedido = pedidoService.getPedido(id);

        if (pedido == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pedido);
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) 
    {
        Pedido guardado = pedidoService.guardar(pedido);
        return ResponseEntity.ok(guardado);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<Pedido>> crearLote(@RequestBody List<Pedido> pedidos) 
    {
        return ResponseEntity.ok(pedidoService.guardarLote(pedidos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable int id, @RequestBody Pedido pedidoActualizado) 
    {
        return ResponseEntity.ok(pedidoService.actualizaPedido(id, pedidoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable int id) 
    {
            pedidoService.borrar(id);
            return ResponseEntity.noContent().build();
    }
}
