package com.proyecto.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.proyecto.model.entity.ContactoPersona;

public interface IContactoPersonaService 
{
	public ContactoPersona buscarPorId(int id); 

	public List<ContactoPersona> listar();
		
	public HashMap<String, Object> llenarSelect2(String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina);
	
	public HashMap<String, Object> llenarDataTableContactoPersona(
		ContactoPersona contactoPersona, 
		String haRecibido, 
		Date ultimaFechaEnvioCorreoDesde, 
		Date ultimaFechaEnvioCorreoHasta, 
		int inicio, 
		int registrosPorPagina
	);
	
	public ContactoPersona guardar(ContactoPersona contactoPersona) throws Exception;

	public void actualizar(ContactoPersona contactoPersona) throws Exception;

	public Boolean borrar(int id);

	public void procesarDatosExcel(List<ContactoPersona> elementosInsertados);	
	
	public String validarEmail(String email);
	
	public Boolean existsByEmail(String email);

	public void validarContactoPersona(ContactoPersona contactoPersona) throws Exception;
}













