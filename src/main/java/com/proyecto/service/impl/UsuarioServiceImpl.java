package com.proyecto.service.impl; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.model.entity.Usuario;
import com.proyecto.model.repository.UsuarioRepository;
import com.proyecto.service.IUsuarioService;


@Service  
public class UsuarioServiceImpl implements IUsuarioService   
{ 
	
	@Autowired private UsuarioRepository usuarioRepository; 
	
	
	@Override
	public HashMap<String, Object> llenarSelect2(String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina) 
	{ 
		try {
			numeroPagina = numeroPagina - 1;
			
			List<Usuario> lista = usuarioRepository.llenarSelect_2(busqueda, PageRequest.of(numeroPagina, registrosPorPagina));

			long totalRegistros = usuarioRepository.contarTotalRegistros_Select_2(busqueda);
			HashMap<String, Object> retorno = new HashMap<>(); 
			retorno.put("Total", totalRegistros); 
			
			List<Object> resultados = new ArrayList<Object>();
			
			for (Usuario c : lista) {
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("id", c.getId());
				mapa.put("text", c.getUsername()); 
				resultados.add(mapa);
			}
			
			retorno.put("Results", resultados); 
			
			return retorno; 
		} 
		catch (Exception ex) {
			throw new RuntimeException("Error en el metodo llenarSelect2. " + ex.getMessage());
		}
	} 

} 



