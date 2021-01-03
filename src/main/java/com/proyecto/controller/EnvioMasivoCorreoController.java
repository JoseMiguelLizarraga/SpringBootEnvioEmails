package com.proyecto.controller; 

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.model.entity.EnvioMasivoCorreo;
import com.proyecto.service.IEnvioMasivoCorreoService; 


@CrossOrigin 
@RestController 
@RequestMapping("/EnvioMasivoCorreo") 
public class EnvioMasivoCorreoController 
{ 

	@Autowired private IEnvioMasivoCorreoService servicio;

	
	private Map<String, Object> llenarInformacionDataTable(EnvioMasivoCorreo envioMasivoCorreo, HttpServletRequest request) 
	{ 
		try { 
			int inicio = Integer.parseInt(request.getParameter("start")); 
			int registrosPorPagina = Integer.parseInt(request.getParameter("length"));  // Es la cantidad de registros por pagina  

			Date fechaEnvioDesde = (StringUtils.hasText(request.getParameter("fechaEnvioDesde"))) ? 
				new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("fechaEnvioDesde")) : null; 

			Date fechaEnvioHasta = (StringUtils.hasText(request.getParameter("fechaEnvioHasta"))) ? 
				new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("fechaEnvioHasta")) : null; 


			Map<String, Object> mapa = servicio.llenarDataTableEnvioMasivoCorreo(envioMasivoCorreo, fechaEnvioDesde, fechaEnvioHasta, inicio, registrosPorPagina); 
			return mapa; 
		} 
		catch(Exception ex) 
		{ 
			throw new IllegalArgumentException(ex.getMessage()); 
		} 
	} 


	@RequestMapping(path = "/llenarDataTable") 
	public @ResponseBody Object llenarDataTable(EnvioMasivoCorreo envioMasivoCorreo, HttpServletRequest request) 
	{ 
		try { 
			Map<String, Object> mapa = llenarInformacionDataTable(envioMasivoCorreo, request); 
			return mapa; 
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@PostMapping(value = "/{enviarPorEmail}") 
	public ResponseEntity guardar
	(
		@RequestBody EnvioMasivoCorreo envioMasivoCorreo, 
		@PathVariable("enviarPorEmail") Boolean enviarPorEmail, 
		BindingResult result
	) 
	{ 
		try { 
			if (result.hasErrors()) throw new Exception(result.getAllErrors().stream().findFirst().get().getDefaultMessage()); 
			
			int idGuardado = servicio.guardar(envioMasivoCorreo);
			
			if (enviarPorEmail) {  				// Si se desea enviar por email	
				servicio.enviarCorreos(idGuardado);
			}
			return new ResponseEntity(HttpStatus.OK);
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@GetMapping(path = {"/{id}"})  // url:    /EnvioMasivoCorreo/1  
	public Object editar(@PathVariable("id") int id) 
	{ 
		try { 
			return servicio.buscarPorId(id);  
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 
	

	@PutMapping(value = "/{enviarPorEmail}")  
	public ResponseEntity editar
	(
		@RequestBody EnvioMasivoCorreo envioMasivoCorreo, 
		@PathVariable("enviarPorEmail") Boolean enviarPorEmail,
		BindingResult result
	) 
	{ 
		try { 
			if (result.hasErrors()) throw new Exception(result.getAllErrors().stream().findFirst().get().getDefaultMessage());  
			servicio.actualizar(envioMasivoCorreo); 
			
			if (enviarPorEmail) {  				// Si se desea enviar por email	
				servicio.enviarCorreos(envioMasivoCorreo.getId());
			}
			
			return new ResponseEntity(HttpStatus.OK);
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@DeleteMapping(path = "/{id}") 
	public Object borrar(@PathVariable("id") int id) 
	{ 
		try { 
			servicio.borrar(id); 
			return new ResponseEntity(HttpStatus.OK); 
		} 
		catch(Exception ex) 
		{ 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

} 



