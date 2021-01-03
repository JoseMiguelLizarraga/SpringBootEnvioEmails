package com.proyecto.service;

import java.util.HashMap;

public interface IUsuarioService 
{
	public HashMap<String, Object> llenarSelect2(
		String atributoBuscado, 
		String busqueda, 
		int registrosPorPagina, 
		int numeroPagina
	);
}
