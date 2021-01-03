package com.proyecto.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.proyecto.model.entity.EnvioMasivoCorreo;
import com.proyecto.model.entity.Usuario;


public interface IEnvioMasivoCorreoService 
{	
	public void enviarCorreos(int id) throws Exception;
	
	public EnvioMasivoCorreo buscarPorId(int id);
	
	public List<EnvioMasivoCorreo> listar();

	public HashMap<String, Object> llenarDataTableEnvioMasivoCorreo(
		EnvioMasivoCorreo envioMasivoCorreo, 
		Date fechaEnvioDesde, 
		Date fechaEnvioHasta, 
		int inicio, 
		int registrosPorPagina
	);

	public int guardar(EnvioMasivoCorreo envioMasivoCorreo) throws Exception;

	public void actualizar(EnvioMasivoCorreo envioMasivoCorreo) throws Exception;

	public Boolean borrar(int id);

	public void validarEnvioMasivoCorreo(EnvioMasivoCorreo envioMasivoCorreo) throws Exception;
		
	public Usuario obtenerUsuarioLogueado();
}
