package com.proyecto.model.repository;

import org.springframework.data.repository.CrudRepository;

import com.proyecto.model.entity.MaestroEnvioEmail;


public interface MaestroEnvioEmailRepository extends CrudRepository<MaestroEnvioEmail, Integer>
{
	public MaestroEnvioEmail findFirstByOrderByIdAsc();  // Busca el primer resultado ordenado por id asc
}
