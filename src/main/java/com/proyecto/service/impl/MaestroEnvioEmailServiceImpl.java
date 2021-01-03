package com.proyecto.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.proyecto.model.entity.MaestroEnvioEmail;
import com.proyecto.model.repository.MaestroEnvioEmailRepository;
import com.proyecto.service.IMaestroEnvioEmailService;


@Service
public class MaestroEnvioEmailServiceImpl implements IMaestroEnvioEmailService
{
	@Autowired 
	private MaestroEnvioEmailRepository repositorio;

	@Override
	public MaestroEnvioEmail buscarPorId(int id) 
	{ 
		try { 
			return repositorio.findById(id).orElse(null);
		} 
		catch(Exception ex) { 
			throw new RuntimeException("Error al buscar MaestroEnvioEmail con el id: " + id + "." + ex.getMessage()); 
		} 
	} 

	@Override
	public Iterable<MaestroEnvioEmail> listar() 
	{ 
		try { 
			return repositorio.findAll();
		} 
		catch(Exception ex) { 
			throw new RuntimeException("Error al listar la entidad MaestroEnvioEmail. " + ex.getMessage()); 
		} 
	} 

	@Override
	public void actualizar(MaestroEnvioEmail maestroEnvioEmail) throws Exception 
	{ 
		validarMaestroEnvioEmail(maestroEnvioEmail);  // Validación 
		
		Optional<MaestroEnvioEmail> optional = repositorio.findById(maestroEnvioEmail.getId());
		
		if (optional.isPresent()) 
		{
			MaestroEnvioEmail objeto = optional.get();

			objeto.setPasswordEmailEnvio(maestroEnvioEmail.getPasswordEmailEnvio()); 
			objeto.setEmailEnvio(maestroEnvioEmail.getEmailEnvio()); 
			objeto.setPuertoEmailEnvio(maestroEnvioEmail.getPuertoEmailEnvio()); 
			objeto.setHostEmailEnvio(maestroEnvioEmail.getHostEmailEnvio()); 

			repositorio.save(objeto);			
		}	
	} 

	@Override
	public void validarMaestroEnvioEmail(MaestroEnvioEmail maestroEnvioEmail) throws Exception 
	{ 
		String mensaje = ""; 

		// Se validan los atributos de la entidad 

		if (! StringUtils.hasText(maestroEnvioEmail.getPasswordEmailEnvio())) mensaje = "El campo passwordEmailEnvio no posee un valor"; 
		if (! StringUtils.hasText(maestroEnvioEmail.getEmailEnvio())) mensaje = "El campo emailEnvio no posee un valor"; 
		if (! StringUtils.hasText(maestroEnvioEmail.getPuertoEmailEnvio())) mensaje = "El campo puertoEmailEnvio no posee un valor"; 
		if (! StringUtils.hasText(maestroEnvioEmail.getHostEmailEnvio())) mensaje = "El campo hostEmailEnvio no posee un valor"; 

		if (mensaje != "") throw new IllegalArgumentException(mensaje); 
	} 

}
