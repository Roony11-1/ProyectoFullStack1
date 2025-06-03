package com.patitofeliz.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.inventory_service.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer>
{

}
