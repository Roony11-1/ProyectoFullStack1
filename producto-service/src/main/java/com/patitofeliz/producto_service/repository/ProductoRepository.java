package com.patitofeliz.producto_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.producto_service.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer> 
{
    
}
