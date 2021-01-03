package com.proyecto.service;

import com.proyecto.model.entity.MaestroEnvioEmail;


public interface IMaestroEnvioEmailService 
{
	public MaestroEnvioEmail buscarPorId(int id);

	public Iterable<MaestroEnvioEmail> listar();

	public void actualizar(MaestroEnvioEmail maestroEnvioEmail) throws Exception;

	public void validarMaestroEnvioEmail(MaestroEnvioEmail maestroEnvioEmail) throws Exception;
}
