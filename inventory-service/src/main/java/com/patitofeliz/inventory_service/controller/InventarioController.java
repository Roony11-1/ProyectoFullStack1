package com.patitofeliz.inventory_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patitofeliz.inventory_service.service.InventarioService;

@RestController
@RequestMapping("/inventario")
public class InventarioController 
{
    @Autowired
    private InventarioService inventarioService;
}
