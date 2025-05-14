package com.patitofeliz.sale_service.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.sale_service.model.Carrito;

@Repository
public interface ReporitoryCarrito extends JpaRepository<Carrito,Integer>  {
    List<Carrito> findByUsuarioId(int id);

}
