package com.proyecto.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.proyecto.model.entity.EnvioCorreoContactoPersona;
import com.proyecto.model.entity.EnvioMasivoCorreo;
import com.proyecto.model.entity.MaestroEnvioEmail;
import com.proyecto.model.entity.Usuario;
import com.proyecto.model.repository.EnvioCorreoContactoPersonaRepository;
import com.proyecto.model.repository.EnvioMasivoCorreoRepository;
import com.proyecto.model.repository.MaestroEnvioEmailRepository;
import com.proyecto.model.repository.UsuarioRepository;
import com.proyecto.service.IEnvioMasivoCorreoService;


@Service
public class EnvioMasivoCorreoServiceImpl implements IEnvioMasivoCorreoService  
{ 
	
	@Autowired private EnvioMasivoCorreoRepository repositorio; 
	@Autowired private EnvioMasivoCorreoRepository repositorio_EnvioMasivoCorreo;
	@Autowired private UsuarioRepository repositorio_Usuario; 	
	@Autowired private EnvioCorreoContactoPersonaRepository repositorio_EnvioCorreoContactoPersona;	
	@Autowired private MaestroEnvioEmailRepository maestroEnvioEmailRepository;
	
	@Autowired private JobLauncher jobLauncher;   // Esto es para poder ejecutar nuestro job	
	@Autowired private Job job;   				  // Inyeccion de la interfaz Job
	
	
	@Override
	public void enviarCorreos(int id) throws Exception
	{
		EnvioMasivoCorreo envioMasivoCorreo = buscarPorId(id);
		
		try {
			if (envioMasivoCorreo.getEstado().equals("finalizado")) {
				throw new Exception("No es posible realizar un envío de correos cuyo estado es 'finalizado'"); 
			}
			if (envioMasivoCorreo.getEstado().equals("en cola")) {
				throw new Exception("No es posible realizar un envío de correos que se encuentra en proceso de envío"); 
			}
			
			if (! repositorio_EnvioCorreoContactoPersona.existsByEnvioMasivoCorreo(envioMasivoCorreo)) {
				throw new Exception("El correo no tiene destinatarios seleccionados"); 
			}
			
			envioMasivoCorreo.setEstado("en cola");
			repositorio.save(envioMasivoCorreo);
	
			// Esto es para enviar parametros al job (Son los parametros de ejecucion)
			
			JobParameters jobParameters = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
			//JobParameters jobParameters= new JobParametersBuilder().addString("idEnvioMasivoCorreo", "aaaaaa").toJobParameters();  
			
			JobExecution execution = jobLauncher.run(job, jobParameters);
			ExitStatus status = execution.getExitStatus();
			
			String estado = "";
			String mensajeError = "";
			
	        if (! ExitStatus.COMPLETED.getExitCode().equals(status.getExitCode()))  // Si el proceso batch capturo un error
	        {
	        	estado = "envio fallido";
	        	mensajeError = "Se encontró un error en el envío de correo";
	        	
	        	//throw new RuntimeException("Se encontró un error en el envío de correo"); 
	        	/*
				List<Throwable> exceptions = execution.getAllFailureExceptions();
				
				for (final Throwable throwable : exceptions) 
				{
					if (throwable instanceof IllegalArgumentException) {
						throw (IllegalArgumentException) throwable;
					}
				}	
				*/		
	        }
	
			if (! repositorio_EnvioCorreoContactoPersona.existsByEnvioMasivoCorreoAndEstadoNot(envioMasivoCorreo, "enviado")) {
				estado = "finalizado";
				envioMasivoCorreo.setFechaEnvio(new Date());
			}
			else {
				estado = "envio fallido";
				mensajeError = "Se encontraron emails sin enviar";
			}
			
			envioMasivoCorreo.setEstado(estado);
    		repositorio_EnvioMasivoCorreo.save(envioMasivoCorreo);
    		
    		if (! mensajeError.equals("")) throw new RuntimeException(mensajeError); 
		} 
		catch (Exception ex) 
		{
			envioMasivoCorreo.setEstado("envio fallido");
    		repositorio_EnvioMasivoCorreo.save(envioMasivoCorreo);
			throw new RuntimeException("Se encontró un error en el envío de correo"); 
		}
	}
	

	@Override
	public EnvioMasivoCorreo buscarPorId(int id) 
	{ 
		try {
			EnvioMasivoCorreo envioMasivoCorreo = repositorio.findById(id);
			
			List<EnvioCorreoContactoPersona> listaEnvioCorreoContactoPersona = repositorio_EnvioCorreoContactoPersona.findAllByEnvioMasivoCorreo(envioMasivoCorreo);
			envioMasivoCorreo.setListaEnvioCorreoContactoPersona(listaEnvioCorreoContactoPersona); 
			
			return envioMasivoCorreo; 
		} 
		catch (Exception ex) {
			throw new RuntimeException("Error al buscar EnvioMasivoCorreo con el id: " + id + "." + ex.getMessage()); 
		}
	} 

	
	@Override
	public List<EnvioMasivoCorreo> listar() 
	{ 
		try { 
			return repositorio.findAll();
		} 
		catch(Exception ex) { 
			throw new RuntimeException("Error al listar la entidad EnvioMasivoCorreo. " + ex.getMessage()); 
		} 	
	} 

	
	@Override
	public HashMap<String, Object> llenarDataTableEnvioMasivoCorreo(EnvioMasivoCorreo envioMasivoCorreo, Date fechaEnvioDesde, Date fechaEnvioHasta, int inicio, int registrosPorPagina) 
	{ 
		try {
			int pagina = (inicio > 0) ? inicio / registrosPorPagina : inicio;  // Obtener la pagina en base a la posicion enviada por el datatable desde el frontend
			
			List<EnvioMasivoCorreo> listaEnvioMasivoCorreo = repositorio.llenarDataTable( 
				envioMasivoCorreo.getAsuntoCorreo(),
				envioMasivoCorreo.getEmailOrigenEnvio(),
				(envioMasivoCorreo.getUsuario() != null && envioMasivoCorreo.getUsuario().getId() != null) ? envioMasivoCorreo.getUsuario().getId() : 0,
				envioMasivoCorreo.getDescripcion(),
				envioMasivoCorreo.getEstado(),
				fechaEnvioDesde,
				fechaEnvioHasta,
				PageRequest.of(pagina, registrosPorPagina) 
			);  
	
			long totalRegistros = repositorio.contarTotalRegistrosDataTable( 
				envioMasivoCorreo.getAsuntoCorreo(),
				envioMasivoCorreo.getEmailOrigenEnvio(),
				(envioMasivoCorreo.getUsuario() != null && envioMasivoCorreo.getUsuario().getId() != null) ? envioMasivoCorreo.getUsuario().getId() : 0,
				envioMasivoCorreo.getDescripcion(),
				envioMasivoCorreo.getEstado(),
				fechaEnvioDesde,
				fechaEnvioHasta
			);  
			
			HashMap<String, Object> mapa = new HashMap<>(); 
			mapa.put("recordsFiltered", totalRegistros); 
			mapa.put("recordsTotal", totalRegistros); 
	
			mapa.put("data", listaEnvioMasivoCorreo); 
	
			return mapa; 
		} 
		catch (Exception ex) {
			throw new RuntimeException("Error al listar la entidad EnvioMasivoCorreo con el metodo llenarDataTable. " + ex.getMessage()); 
		}
	} 

	
	@Override
	@Transactional
	public int guardar(EnvioMasivoCorreo envioMasivoCorreo) throws Exception 
	{ 
		MaestroEnvioEmail maestroEnvioEmail = maestroEnvioEmailRepository.findFirstByOrderByIdAsc();  
		
		if (! StringUtils.hasText(envioMasivoCorreo.getDescripcion())) {  envioMasivoCorreo.setDescripcion("");  }
		
		envioMasivoCorreo.setFechaEnvio(null);
		envioMasivoCorreo.setUsuario( obtenerUsuarioLogueado() );  // Obtiene el usuario que hizo su login
		envioMasivoCorreo.setEmailOrigenEnvio(maestroEnvioEmail.getEmailEnvio());
		envioMasivoCorreo.setEstado("sin enviar");
		
		//===============================================================>>>>>
		
		validarEnvioMasivoCorreo(envioMasivoCorreo);  // Validación 

		try { 
			repositorio.save(envioMasivoCorreo);
			
			// Referencias Cruzadas 

			if (envioMasivoCorreo.getListaEnvioCorreoContactoPersona() != null && envioMasivoCorreo.getListaEnvioCorreoContactoPersona().size() > 0)  // EnvioCorreoContactoPersona 
			{
				for(EnvioCorreoContactoPersona detalle : envioMasivoCorreo.getListaEnvioCorreoContactoPersona()) { 
					detalle.setEnvioMasivoCorreo(envioMasivoCorreo); 
					repositorio_EnvioCorreoContactoPersona.save(detalle);
				} 
			} 
			
			return envioMasivoCorreo.getId(); 
		} 
		catch (Exception ex) 
		{ 
			throw new RuntimeException("No se pudo insertar el registro. " + ex.getMessage()); 
		} 
	} 


	@Override
	@Transactional
	public void actualizar(EnvioMasivoCorreo envioMasivoCorreo) throws Exception 
	{ 
		MaestroEnvioEmail maestroEnvioEmail = maestroEnvioEmailRepository.findFirstByOrderByIdAsc();
		validarEnvioMasivoCorreo(envioMasivoCorreo);  // Validación 

		try { 
			
			EnvioMasivoCorreo objeto = buscarPorId(envioMasivoCorreo.getId()); 

			objeto.setAsuntoCorreo(envioMasivoCorreo.getAsuntoCorreo()); 
			objeto.setEmailOrigenEnvio(maestroEnvioEmail.getEmailEnvio()); 
			objeto.setUsuario(envioMasivoCorreo.getUsuario()); 
			objeto.setDescripcion(envioMasivoCorreo.getDescripcion()); 
			objeto.setEstado(envioMasivoCorreo.getEstado()); 
			objeto.setFechaEnvio(envioMasivoCorreo.getFechaEnvio()); 
			objeto.setContenidoCorreo(envioMasivoCorreo.getContenidoCorreo()); 

			repositorio.save(objeto);

			// Referencias Cruzadas de la entidad EnvioCorreoContactoPersona 

			List<EnvioCorreoContactoPersona> listaEnvioCorreoContactoPersona = envioMasivoCorreo.getListaEnvioCorreoContactoPersona(); 

			if (listaEnvioCorreoContactoPersona != null && listaEnvioCorreoContactoPersona.size() > 0) 
			{ 
				// Si solo se recibio un item y es nuevo 
				if (listaEnvioCorreoContactoPersona.size() == 1 && listaEnvioCorreoContactoPersona.stream().anyMatch(c-> c.getId() == null)) { 
					//repositorio_EnvioCorreoContactoPersona.deleteByEnvioMasivoCorreo(envioMasivoCorreo);
					repositorio_EnvioCorreoContactoPersona.deleteByEnvioMasivoCorreoId(envioMasivoCorreo.getId());
				} 
				if (listaEnvioCorreoContactoPersona.stream().anyMatch(c-> c.getId() != null))  // Si al menos un detalle trae id 
				{ 
					// Se borran los detalles almacenados cuyos ids no esten en los ids de los detalles actualizados
					
					repositorio_EnvioCorreoContactoPersona.deleteByEnvioMasivoCorreoAndIds(
						envioMasivoCorreo.getId(),
						listaEnvioCorreoContactoPersona.stream().filter(a-> a.getId() != null).map(c -> c.getId()).collect(Collectors.toList())
					);
				} 
				for(EnvioCorreoContactoPersona detalle : listaEnvioCorreoContactoPersona) 
				{ 
					if (detalle.getId() != null)  // Si trae id significa que esta almacenado 
					{ 
						EnvioCorreoContactoPersona detalleBuscado = repositorio_EnvioCorreoContactoPersona.findById(detalle.getId()).orElse(null);
						
						detalleBuscado.setEstado(detalle.getEstado()); 
						detalleBuscado.setContactoPersona(detalle.getContactoPersona()); 
						repositorio_EnvioCorreoContactoPersona.save(detalleBuscado);
					} 
					else   // Si no esta guardado el detalle recorrido, se agrega 
					{ 
						detalle.setEnvioMasivoCorreo(envioMasivoCorreo); 
						repositorio_EnvioCorreoContactoPersona.save(detalle);
 					} 
				} 
			} 
			else  // Si no se recibieron detalles de la clase EnvioCorreoContactoPersona 
			{ 
				repositorio_EnvioCorreoContactoPersona.deleteByEnvioMasivoCorreoId(envioMasivoCorreo.getId());
			} 
		} 
		catch (Exception ex) { 
			throw new RuntimeException("No se pudo actualizar el registro. " + ex.getMessage()); 
		} 
	} 


	@Override
	@Transactional
	public Boolean borrar(int id) 
	{ 
		try { 
			repositorio_EnvioCorreoContactoPersona.deleteByEnvioMasivoCorreoId(id);  // Se borran las referencias de la tabla EnvioCorreoContactoPersona 

			repositorio.deleteById(id);
			return true; 
		} 
		catch (Exception ex) 
		{ 
			throw new RuntimeException("No se pudo borrar el EnvioMasivoCorreo con el id " + id + ". " + ex.getMessage()); 
		} 
	} 


	@Override
	public void validarEnvioMasivoCorreo(EnvioMasivoCorreo envioMasivoCorreo) throws Exception 
	{ 
		String mensaje = ""; 
		List<EnvioCorreoContactoPersona> listaEnvioCorreoContactoPersona = envioMasivoCorreo.getListaEnvioCorreoContactoPersona(); 

		// Se validan los atributos de la entidad 

		if (! StringUtils.hasText(envioMasivoCorreo.getContenidoCorreo())) mensaje = "El correo no tiene un contenido"; 
		else if (envioMasivoCorreo.getContenidoCorreo().length() > 100000) mensaje = "El contenido del correo no puede superar los 100.000 caracteres. Probablemente usted adjuntó una imagen copiada de su computadora";
		else if (! StringUtils.hasText(envioMasivoCorreo.getAsuntoCorreo())) mensaje = "El campo asuntoCorreo no posee un valor"; 
		else if (! StringUtils.hasText(envioMasivoCorreo.getEmailOrigenEnvio())) mensaje = "El campo emailOrigenEnvio no posee un valor"; 
		else if (! StringUtils.hasText(envioMasivoCorreo.getEstado())) mensaje = "El campo estado no posee un valor"; 
		else if (envioMasivoCorreo.getUsuario() == null || envioMasivoCorreo.getUsuario().getId() == null) mensaje = "El campo de tipo Usuario no está seleccionado"; 
		else if (listaEnvioCorreoContactoPersona == null || listaEnvioCorreoContactoPersona.size() == 0) mensaje = "El correo no tiene destinatarios"; 
		
		if (mensaje != "") throw new IllegalArgumentException(mensaje); 

		// Se validan las referencias cruzadas de la entidad EnvioCorreoContactoPersona 
		listaEnvioCorreoContactoPersona.forEach(c-> 
		{ 
			if (! StringUtils.hasText(c.getEstado())) throw new IllegalArgumentException("Un detalle de tipo EnvioCorreoContactoPersona tiene vacío el campo estado"); 
			if (c.getContactoPersona() == null || c.getContactoPersona().getId() == null) throw new IllegalArgumentException("Un detalle de tipo EnvioCorreoContactoPersona no tiene seleccionado el campo contactoPersona"); 
		}); 
	} 
	
	
	@Override
	public Usuario obtenerUsuarioLogueado()
	{
		if (
		 SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
		 !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) &&
		 SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
		) 
		{
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return repositorio_Usuario.findByUsername(userDetails.getUsername());
		}

		return null;
	}


} 



















