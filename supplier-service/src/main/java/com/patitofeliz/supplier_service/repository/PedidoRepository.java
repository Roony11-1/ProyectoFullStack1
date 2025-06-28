package com.patitofeliz.supplier_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.supplier_service.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>
{

}
