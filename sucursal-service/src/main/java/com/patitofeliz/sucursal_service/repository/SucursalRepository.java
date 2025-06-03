package com.patitofeliz.sucursal_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.sucursal_service.model.Sucursal;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer>
{

}
