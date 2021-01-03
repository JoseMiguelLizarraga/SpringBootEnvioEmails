package com.proyecto.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.dto.DetalleRespuestaApiValidarEmailDTO;
import com.proyecto.dto.RespuestaApiValidarEmailDTO;
import com.proyecto.model.entity.ContactoPersona;
import com.proyecto.model.repository.ContactoPersonaRepository;
import com.proyecto.model.repository.EnvioCorreoContactoPersonaRepository;
import com.proyecto.service.IContactoPersonaService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


@Service
public class ContactoPersonaServiceImpl implements IContactoPersonaService
{ 

	@Autowired 
	private SessionFactory sessionFactory; 
	
	@Autowired
	private ContactoPersonaRepository repositorio;
	
	@Autowired
	private EnvioCorreoContactoPersonaRepository envioCorreoContactoPersonaRepository;


	@Override
	public ContactoPersona buscarPorId(int id) 
	{ 
		try { 
			return repositorio.findById(id);
		} 
		catch(Exception ex) { 
			throw new RuntimeException("Error al buscar ContactoPersona con el id: " + id + "." + ex.getMessage()); 
		} 
	} 


	@Override
	public List<ContactoPersona> listar() 
	{ 
		try { 
			return repositorio.findAll();
		} 
		catch(Exception ex) { 
			throw new RuntimeException("Error al listar la entidad ContactoPersona. " + ex.getMessage()); 
		} 
	} 
	
	
	@Override
	public HashMap<String, Object> llenarSelect2(String atributoBuscado, String busqueda, int registrosPorPagina, int numeroPagina) 
	{ 
		try {
			DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
			numeroPagina = numeroPagina - 1;
			
			List<ContactoPersona> lista = repositorio.llenarSelect_2(busqueda, true, PageRequest.of(numeroPagina, registrosPorPagina));

			long totalRegistros = repositorio.contarTotalRegistros_Select_2(busqueda, true);
			HashMap<String, Object> retorno = new HashMap<>(); 
			retorno.put("Total", totalRegistros); 
			
			List<Object> resultados = new ArrayList<Object>();
			
			for (ContactoPersona c : lista) {
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("id", c.getId());
				mapa.put("text", c.getEmail() + "   ----   " + (c.getUltimaFechaEnvioCorreo() != null ? "Si":"No") + " enviado   ----   Última fecha de envío: " + (c.getUltimaFechaEnvioCorreo() != null ? formatoFecha.format(c.getUltimaFechaEnvioCorreo()):"no tiene")); 
				resultados.add(mapa);
			}
			
			retorno.put("Results", resultados); 
			
			return retorno; 
		} 
		catch (Exception ex) {
			throw new RuntimeException("Error en el metodo llenarSelect2. " + ex.getMessage());
		}
	} 
	

	@Override
	public HashMap<String, Object> llenarDataTableContactoPersona(ContactoPersona contactoPersona, String haRecibido, Date ultimaFechaEnvioCorreoDesde, Date ultimaFechaEnvioCorreoHasta, int inicio, int registrosPorPagina) 
	{ 
		Session session = sessionFactory.openSession(); 

		try{ 
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); 
			CriteriaQuery<ContactoPersona> cr = criteriaBuilder.createQuery(ContactoPersona.class); 
			Root<ContactoPersona> root = cr.from(ContactoPersona.class); 

			cr.select(root);  // Asi llama todos los atributos de la entidad 

			List<Predicate> predicates = new ArrayList<Predicate>(); 
			
			if (StringUtils.hasText(haRecibido))  
			{ 
				if (haRecibido.equals("true")) {
					predicates.add(criteriaBuilder.isNotNull(root.get("ultimaFechaEnvioCorreo")));  // Busca los registros cuyo campo ultimaFechaEnvioCorreo no sea nulo
				} 
				else {
					predicates.add(criteriaBuilder.isNull(root.get("ultimaFechaEnvioCorreo")));  // Busca los registros cuyo campo ultimaFechaEnvioCorreo sea nulo
				}
			} 
			if (contactoPersona.getHaRespondido() != null) { 
				predicates.add(criteriaBuilder.equal(root.get("haRespondido"), contactoPersona.getHaRespondido())); 
			} 
			if (ultimaFechaEnvioCorreoDesde != null) { 
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("ultimaFechaEnvioCorreo"), ultimaFechaEnvioCorreoDesde)); 
			} 
			if (ultimaFechaEnvioCorreoHasta != null) { 
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("ultimaFechaEnvioCorreo"), ultimaFechaEnvioCorreoHasta)); 
			} 
			if (StringUtils.hasText(contactoPersona.getNombreCompleto())) { 
				predicates.add(criteriaBuilder.like(root.get("nombreCompleto"), "%" + contactoPersona.getNombreCompleto() + "%")); 
			} 
			if (StringUtils.hasText(contactoPersona.getEmail())) { 
				predicates.add(criteriaBuilder.like(root.get("email"), "%" + contactoPersona.getEmail() + "%")); 
			} 
			if (contactoPersona.getExiste() != null) { 
				predicates.add(criteriaBuilder.equal(root.get("existe"), contactoPersona.getExiste())); 
			} 

			cr.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {}))); 
			cr.orderBy(criteriaBuilder.desc(root.get("id")));    // Ordena por id descendente           

			int totalRegistros = session.createQuery(cr).getResultList().size(); 

			List<ContactoPersona> listaContactoPersona = session.createQuery(cr) 
				.setFirstResult(inicio) 				// Delimito la busqueda con el inicio 
				.setMaxResults(registrosPorPagina) 	// Delimito la busqueda con el total de registros 
				.getResultList(); 

			HashMap<String, Object> mapa = new HashMap<>(); 
			mapa.put("recordsFiltered", totalRegistros); 
			mapa.put("recordsTotal", totalRegistros); 

			mapa.put("data", listaContactoPersona); 

			return mapa; 
		} 
		catch(Exception ex){ 
			throw new RuntimeException("Error al listar la entidad ContactoPersona con el metodo llenarDataTable. " + ex.getMessage()); 
		} 
		finally{ 
			session.close(); 
		} 
	} 

	
	@Override
	@Transactional
	public ContactoPersona guardar(ContactoPersona contactoPersona) throws Exception 
	{ 
		validarContactoPersona(contactoPersona);  // Validación 

		try { 
			repositorio.save(contactoPersona);
			return contactoPersona; 
		} 
		catch (Exception ex) 
		{ 
			throw new RuntimeException("No se pudo insertar el registro. " + ex.getMessage()); 
		} 
	} 


	@Override
	@Transactional
	public void actualizar(ContactoPersona contactoPersona) throws Exception 
	{ 
		validarContactoPersona(contactoPersona);  // Validación 
		ContactoPersona objeto = this.buscarPorId(contactoPersona.getId());
		
		try { 
			objeto.setHaRespondido(contactoPersona.getHaRespondido()); 
			objeto.setUltimaFechaRespuesta(contactoPersona.getUltimaFechaRespuesta()); 
			objeto.setUltimaFechaEnvioCorreo(contactoPersona.getUltimaFechaEnvioCorreo()); 
			objeto.setNombreCompleto(contactoPersona.getNombreCompleto()); 
			objeto.setEmail(contactoPersona.getEmail()); 
			objeto.setExiste(contactoPersona.getExiste()); 
			
			repositorio.save(objeto); 
		} 
		catch(Exception ex) { 
			throw new RuntimeException("No se pudo actualizar el registro. " + ex.getMessage()); 
		} 
	} 


	@Override
	@Transactional
	public Boolean borrar(int id) 
	{ 
		ContactoPersona contactoPersona = this.buscarPorId(id);
		
		try { 
			// Se borran las referencias de la tabla EnvioCorreoContactoPersona 
			envioCorreoContactoPersonaRepository.deleteByContactoPersona(contactoPersona);
			
			repositorio.delete(contactoPersona);
			return true; 
		} 
		catch (Exception ex) 
		{ 
			throw new RuntimeException("No se pudo borrar el ContactoPersona con el id " + id + ". " + ex.getMessage()); 
		} 
	} 


	@Override
	public void procesarDatosExcel(List<ContactoPersona> elementosInsertados) 
	{ 
//		int cont = 1;
//		for(ContactoPersona contactoPersona : elementosInsertados) {  
//			System.out.println("INSERT INTO contacto_email(id, email, revisado, existe, error_encontrado, status_peticion) VALUES ("+cont+", '"+contactoPersona.getEmail()+"', false, false, '', '');"); 
//			cont ++;
//		} 
		
		Session sesion = null; 
		Transaction tx = null; 

		try { 
			sesion = sessionFactory.openSession(); 
			tx = sesion.beginTransaction(); 

			for(ContactoPersona contactoPersona : elementosInsertados) {  
				sesion.save(contactoPersona);  
			} 

			tx.commit(); 
			sesion.close(); 
		} 
		catch (Exception ex) { 
			tx.rollback(); 
			throw new RuntimeException("Error en el metodo procesarDatosExcel. " + ex.getMessage()); 
		} 
	} 
	
	
	@Override
	@Transactional
	public String validarEmail(String email) 
	{
		try {		
			String URL_API = "https://verify-email.org/home/verify-as-guest/" + email;
			 
			Client client = Client.create();
			WebResource webResource = client.resource(URL_API);	 
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			 
			if(response.getStatus() == 200) 
			{
				ObjectMapper mapper = new ObjectMapper();
				String jsonResponse = response.getEntity(String.class);  // {"email":"juana@gmail.com","response":{"status":1,"log":"Success"},"credits":3}
				 
				RespuestaApiValidarEmailDTO respuestaApi = mapper.readValue(jsonResponse, RespuestaApiValidarEmailDTO.class);
				
				if (respuestaApi.getResponse() != null) 
				{
					DetalleRespuestaApiValidarEmailDTO detalle = respuestaApi.getResponse();

					if (detalle.getStatus() == 1 && detalle.getLog().equals("Success")) 
					{
						repositorio.actualizarEmailValidado(email);
						return "El email " + email + " si existe y ha sido registrado";
					}
					if (detalle.getStatus() != 1 && detalle.getLog() != null) {
						throw new IllegalArgumentException( "El email " + email + " no existe. Datos:" + detalle.getLog().replace("'", "") );
					}
				}		
			}
			else {
				throw new IllegalArgumentException("El email " + email + " fue rechazado");
			}
		} 
		catch (Exception ex) 
		{
			throw new RuntimeException("Se encontro un error al validar el email " + email);
		}
		
		return "";
	}
	
	
	@Override
	public Boolean existsByEmail(String email)
	{
		return repositorio.existsByEmail(email);
	}

	
	@Override
	public void validarContactoPersona(ContactoPersona contactoPersona) throws Exception 
	{ 
		String mensaje = ""; 
		if (! StringUtils.hasText(contactoPersona.getEmail())) mensaje = "El campo email no posee un valor"; 
		
		if (repositorio.existsByEmailAndIdNot(contactoPersona.getEmail(), contactoPersona.getId())) {
			mensaje = "Ya existe un registro de tipo ContactoPersona con ese email"; 
		}

		if (mensaje != "") throw new IllegalArgumentException(mensaje); 

	} 


} 