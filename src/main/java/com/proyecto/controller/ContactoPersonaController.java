package com.proyecto.controller; 


import com.proyecto.model.entity.ContactoPersona; 
import com.proyecto.service.IContactoPersonaService;

import java.util.Arrays; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.Map; 
import java.util.Date; 
import java.text.SimpleDateFormat; 
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.validation.BindingResult; 

// Excel 
import org.apache.poi.hssf.usermodel.HSSFCell; 
import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Otras importaciones necesarias para trabajar con archivos 
import org.springframework.web.multipart.MultipartFile; 


@CrossOrigin 
@RestController 
@RequestMapping("/ContactoPersona") 
public class ContactoPersonaController 
{ 
	
	@Autowired private IContactoPersonaService servicio;

	
	@PostMapping(path = {"/validarEmail/{email}"})  // url:    /ContactoPersona/validarEmail/juanaperez@gmail.com  
	public Object validarEmail(@PathVariable("email") String email) 
	{ 
		try { 
			return servicio.validarEmail(email);
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 
	
	
	@GetMapping 
	public ResponseEntity<Object> listar()  // url:    /ContactoPersona/ 
	{ 
		try { 
			return ResponseEntity.ok(servicio.listar()); 
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

	
	private Map<String, Object> llenarInformacionDataTable(ContactoPersona contactoPersona, HttpServletRequest request) 
	{ 
		try { 
			int inicio = Integer.parseInt(request.getParameter("start")); 
			int registrosPorPagina = Integer.parseInt(request.getParameter("length"));  // Es la cantidad de registros por pagina  

			String haRecibido = request.getParameter("haRecibido");  // Agregado extra

			Date ultimaFechaEnvioCorreoDesde = (StringUtils.hasText(request.getParameter("ultimaFechaEnvioCorreoDesde"))) ? 
				new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("ultimaFechaEnvioCorreoDesde")) : null; 

			Date ultimaFechaEnvioCorreoHasta = (StringUtils.hasText(request.getParameter("ultimaFechaEnvioCorreoHasta"))) ? 
				new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("ultimaFechaEnvioCorreoHasta")) : null; 
	
			Map<String, Object> mapa = servicio.llenarDataTableContactoPersona(contactoPersona, haRecibido, ultimaFechaEnvioCorreoDesde, ultimaFechaEnvioCorreoHasta, inicio, registrosPorPagina); 
			return mapa; 
		} 
		catch(Exception ex) 
		{ 
			throw new IllegalArgumentException(ex.getMessage()); 
		} 
	} 


	@RequestMapping(path = "/llenarDataTable") 
	public @ResponseBody Object llenarDataTable(ContactoPersona contactoPersona, HttpServletRequest request) 
	{ 
		try { 
			Map<String, Object> mapa = llenarInformacionDataTable(contactoPersona, request); 
			return mapa; 
		} 
		catch (Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 
	
	
	@RequestMapping(value = "/llenarSelect2") 
	public @ResponseBody Object llenarSelect2(String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina) 
	{ 
		try { 
			return servicio.llenarSelect2(atributoBuscado, busqueda, registrosPorPagina, numeroPagina); 
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@PostMapping 
	public ResponseEntity<Object> guardar(@RequestBody ContactoPersona contactoPersona, BindingResult result) 
	{ 
		try { 
			if (result.hasErrors()) throw new Exception(result.getAllErrors().stream().findFirst().get().getDefaultMessage());  
			return ResponseEntity.ok(servicio.guardar(contactoPersona)); 
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@GetMapping(path = {"/{id}"})  // url:    /ContactoPersona/1  
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
	public ResponseEntity<String> editar(@RequestBody ContactoPersona contactoPersona, BindingResult result) 
	{ 
		try { 
			if (result.hasErrors()) throw new Exception(result.getAllErrors().stream().findFirst().get().getDefaultMessage());  
			servicio.actualizar(contactoPersona); 
			return new ResponseEntity<String>(HttpStatus.OK); 
		} 
		catch(IllegalArgumentException ex) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 


	@DeleteMapping(path = "/{id}") 
	public ResponseEntity<String> borrar(@PathVariable("id") int id) 
	{ 
		try { 
			servicio.borrar(id); 
			return new ResponseEntity<String>(HttpStatus.OK); 
		} 
		catch(Exception ex) 
		{ 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 



	@PostMapping(value = "/importarExcel") 
	public ResponseEntity<String> importarExcel(@RequestParam("archivo") MultipartFile archivo) 
	{ 
		try { 
			List<String> letras = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"); 
			List<ContactoPersona> listaContactoPersona = new ArrayList<ContactoPersona>(); 
			
			int contadorFila = 0;
			//int contadorFila = 2;  // Esto es porque empieza a contar desde la fila 2 
			
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(archivo.getInputStream());
			
			XSSFSheet worksheet = workbook.getSheetAt(0);

			for(int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) 
			{ 
				XSSFRow row = worksheet.getRow(i); 
				for(int x = 0; x < row.getLastCellNum(); x++) 
				{ 
					Cell celda = row.getCell(x); 
					if (celda == null || celda.getCellType() == HSSFCell.CELL_TYPE_BLANK) { 
						throw new IllegalArgumentException("La fila "+ contadorFila +" en la columna " + letras.get(x) + " tiene un dato incompleto"); 
					} 
					if (celda.getCellType() == HSSFCell.CELL_TYPE_ERROR) { 
						throw new IllegalArgumentException("La fila "+ contadorFila +" en la columna " + letras.get(x) + " tiene un dato no válido"); 
					} 
					celda.setCellType(Cell.CELL_TYPE_STRING);  // Convierte la celda recorrida a String 
				} 
				
				String email = row.getCell(0).getStringCellValue(); 
				
				if (servicio.existsByEmail(email)) {
					throw new IllegalArgumentException("El email " + email + " ya se encuentra registrado"); 
				}

				ContactoPersona contactoPersona = new ContactoPersona(); 

				contactoPersona.setHaRespondido(false); 
				contactoPersona.setUltimaFechaRespuesta(null); 
				contactoPersona.setId(0); 
				contactoPersona.setUltimaFechaEnvioCorreo(null); 
				contactoPersona.setNombreCompleto(""); 
				contactoPersona.setEmail(email); 
				contactoPersona.setExiste(false);      // Se debera validar si existe para poder usar
				listaContactoPersona.add(contactoPersona); 
				contadorFila ++; 
			} 
			
			workbook.close();

			servicio.procesarDatosExcel(listaContactoPersona); 
			
			return new ResponseEntity<String>(HttpStatus.OK); 
		} 
		catch(IllegalArgumentException ex) 
		{ 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
		} 
		catch(Exception ex) 
		{ 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); 
		} 
	} 

} 



