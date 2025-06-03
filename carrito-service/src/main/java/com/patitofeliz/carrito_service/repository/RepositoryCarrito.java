package com.patitofeliz.carrito_service.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.carrito_service.model.Carrito;

@Repository
public interface RepositoryCarrito extends JpaRepository<Carrito,Integer>  {
    List<Carrito> findByUsuarioId(int id);

}
