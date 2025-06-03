package com.patitofeliz.sucursal_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.sucursal_service.repository.SucursalRepository;

@Service
public class SucursalService 
{
    @Autowired
    private SucursalRepository sucursalRepository;
}
