package com.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringBootJwtApplication {

	public static void main(String[] args) 
	{
		/*
		System.out.println("=========================================================>>>>>");
		
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptado = passwordEncoder.encode("cartasjw1453");
        System.out.println(passwordEncriptado);
        
        System.out.println("=========================================================>>>>>");
		*/
		
		SpringApplication.run(SpringBootJwtApplication.class, args);
	}

}
