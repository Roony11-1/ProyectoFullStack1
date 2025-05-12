package com.patitofeliz.sale_service.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.sale_service.model.Carrito;

import java.util.List;

@repository
public interface ReporitoryCarrito extends JpaRepository<Carrito,Integer>  {
    Carrito findByUsuarioId(int id);

}
