package com.patitofeliz.sale_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.sale_service.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer>
{
    List<Venta> findByUsuarioId(int usuarioId);
    List<Venta> findByVendedorId(int vendedorId);
}
