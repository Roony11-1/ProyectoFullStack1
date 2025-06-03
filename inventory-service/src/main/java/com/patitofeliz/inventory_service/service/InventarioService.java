package com.patitofeliz.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.inventory_service.repository.InventarioRepository;

@Service
public class InventarioService 
{
    @Autowired
    private InventarioRepository inventarioRepository;
}
