package com.patitofeliz.supplier_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.patitofeliz.supplier_service.service.ProveedorService;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController 
{
    @Autowired
    private ProveedorService proveedorService;
}
