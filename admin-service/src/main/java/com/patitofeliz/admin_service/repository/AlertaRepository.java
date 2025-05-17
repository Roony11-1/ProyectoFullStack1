package com.patitofeliz.admin_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.admin_service.model.Alerta;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Integer>
{
    List<Alerta> findByTipoAlertaContainingIgnoreCase(String tipoAlertaParcial);
}
