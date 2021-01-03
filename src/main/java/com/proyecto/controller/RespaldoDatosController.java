package com.proyecto.controller; 

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.service.IRespaldoDatosService; 


@CrossOrigin 
@RestController 
public class RespaldoDatosController 
{ 

	@Autowired private IRespaldoDatosService servicio;
	
	
	@RequestMapping(value = "/RespaldoDatos/{clave}", method = RequestMethod.POST, produces=MediaType.APPLICATION_OCTET_STREAM_VALUE) 
	public ResponseEntity<InputStreamResource> respaldoDB(@PathVariable("clave") String clave, HttpServletRequest request) 
	{ 
		try { 
			if (clave.equals("jw2020")) 
			{
				String cadena = servicio.generarCodigoSQL_Insertar();	
		    	InputStream inputStream = new ByteArrayInputStream(cadena.toString().getBytes("UTF-8"));
		    	
				ByteArrayInputStream salida = new ByteArrayInputStream(IOUtils.toByteArray(inputStream));
				
				HttpHeaders headers = new HttpHeaders(); 
				headers.add("Content-Disposition", "attachment; filename=respaldo.sql"); 
	
				inputStream.close();
				
				return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM) 
					.body(new InputStreamResource(salida)); 
			} 
			else {
				throw new Exception("Clave incorrecta");
			}		
		} 
		catch(Exception ex) 
		{ 
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR); 
		} 
	} 
	
} 



