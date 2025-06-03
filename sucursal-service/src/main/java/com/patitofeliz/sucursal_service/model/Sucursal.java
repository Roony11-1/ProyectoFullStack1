package com.patitofeliz.sucursal_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Sucursal 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    
}
