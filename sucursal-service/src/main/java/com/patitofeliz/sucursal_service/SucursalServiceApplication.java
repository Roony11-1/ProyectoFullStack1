package com.patitofeliz.sucursal_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.patitofeliz.sucursal_service",
    "com.patitofeliz.main.client"})

public class SucursalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SucursalServiceApplication.class, args);
	}
	
}
