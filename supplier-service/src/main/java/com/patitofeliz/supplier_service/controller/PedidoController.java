package com.patitofeliz.supplier_service.controller;

import com.patitofeliz.supplier_service.model.Pedido;
import com.patitofeliz.supplier_service.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController 
{

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<EntityModel<Pedido>>> getPedidos() 
    {
        List<Pedido> pedidos = pedidoService.getPedidos();

        if (pedidos.isEmpty())
            return ResponseEntity.noContent().build();
        
        return ResponseEntity.ok(hateoasPlural(pedidos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> getPedido(@PathVariable int id) 
    {
        Pedido pedido = pedidoService.getPedido(id);

        if (pedido == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(hateoasSingular(pedido));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> crear(@RequestBody Pedido pedido) 
    {
        Pedido guardado = pedidoService.guardar(pedido);
        return ResponseEntity.ok(hateoasSingular(guardado));
    }

    @PostMapping("/lote")
    public ResponseEntity<List<EntityModel<Pedido>>> crearLote(@RequestBody List<Pedido> pedidos) 
    {
        return ResponseEntity.ok(hateoasPlural(pedidoService.guardarLote(pedidos)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> actualizar(@PathVariable int id, @RequestBody Pedido pedidoActualizado) 
    {
        return ResponseEntity.ok(hateoasSingular(pedidoService.actualizaPedido(id, pedidoActualizado)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable int id) 
    {
            pedidoService.borrar(id);
            return ResponseEntity.noContent().build();
    }

    // Metodos que me entregan los hateoas -- me chorie de ponerlos uno a uno xD como los weones ya basta
    private EntityModel<Pedido> hateoasSingular(Pedido pedido) 
    {
        int id = pedido.getId();

        return EntityModel.of(pedido,
            linkTo(methodOn(PedidoController.class).getPedidos()).withRel("pedidos")
        );
    }

    private List<EntityModel<Pedido>> hateoasPlural(List<Pedido> pedidos) 
    {
        List<EntityModel<Pedido>> listaconLinks = new ArrayList<>();
        
        for (Pedido pedido : pedidos) 
        {
            EntityModel<Pedido> recurso = EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).getPedido(pedido.getId())).withSelfRel()
            );
            listaconLinks.add(recurso);
        }

        return listaconLinks;
    }
}
