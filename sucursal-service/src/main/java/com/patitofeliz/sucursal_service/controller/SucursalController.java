package com.patitofeliz.sucursal_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patitofeliz.sucursal_service.service.SucursalService;

@RestController
@RequestMapping("/sucursal")
public class SucursalController 
{
    @Autowired
    private SucursalService sucursalService;
}
