package com.patitofeliz.sucursal_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patitofeliz.sucursal_service.model.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Integer>
{

}
