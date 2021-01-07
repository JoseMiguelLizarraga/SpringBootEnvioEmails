package com.proyecto.controller; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.model.entity.MaestroEnvioEmail;
import com.proyecto.service.IMaestroEnvioEmailService; 


@CrossOrigin 
@RestController 
@RequestMapping("/MaestroEnvioEmail") 
public class MaestroEnvioEmailController 
{ 

	@Autowired private IMaestroEnvioEmailService servicio;
	

	@GetMapping 
	public ResponseEntity<Object> listar()  // url:    /MaestroEnvioEmail/ 
	{ 
		try { 
			return ResponseEntity.ok(servicio.listar()); 
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

	@GetMapping(path = {"/{id}"})  // url:    /MaestroEnvioEmail/1  
	public ResponseEntity<Object> editar(@PathVariable("id") int id) 
	{ 
		try { 
			return ResponseEntity.ok(servicio.buscarPorId(id));  
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

	@PutMapping 
	@ResponseBody 
	public ResponseEntity<String> editar(@RequestBody MaestroEnvioEmail maestroEnvioEmail, BindingResult result) 
	{ 
		try { 
			if (result.hasErrors()) throw new Exception(result.getAllErrors().stream().findFirst().get().getDefaultMessage());  
			servicio.actualizar(maestroEnvioEmail); 

			return new ResponseEntity<String>(HttpStatus.OK); 
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

} 



