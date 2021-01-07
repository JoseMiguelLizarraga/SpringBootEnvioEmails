package com.proyecto.service.impl;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.model.entity.Cargo;
import com.proyecto.model.entity.ContactoPersona;
import com.proyecto.model.entity.EnvioCorreoContactoPersona;
import com.proyecto.model.entity.EnvioMasivoCorreo;
import com.proyecto.model.entity.MaestroEnvioEmail;
import com.proyecto.model.entity.ParametrosLogin;
import com.proyecto.model.entity.Usuario;
import com.proyecto.model.entity.UsuarioCargo;
import com.proyecto.service.IRespaldoDatosService;


@Service
public class RespaldoDatosServiceImpl implements IRespaldoDatosService
{ 
	
	@Autowired private SessionFactory sessionFactory; 

	
	@SuppressWarnings("unchecked")
	@Override
	public String generarCodigoSQL_Insertar() throws Exception 
	{ 

		Session sesion = sessionFactory.openSession(); 

		try { 
			StringBuilder cadena = new StringBuilder(); 
			
			cadena.append("\n\n-- ParametrosLogin \n"); 
			List<ParametrosLogin> listaParametrosLogin = sesion.createQuery( 
				"select c from ParametrosLogin c " 
			).list(); 
			
			for (ParametrosLogin c : listaParametrosLogin) 
			{ 
				cadena.append("INSERT INTO parametros_login(" + 
					"id, " + 
					"mensaje_error_autenticacion, " + 
					"duracion_token_milisegundos, " + 
					"mensaje_error_interno, " + 
					"mensaje_usuario_deshabilitado, " + 
					"mensaje_token_expirado" + 
				") " + 
				"VALUES (" + 
					"" + c.getId() + ", " + 
					"'" + c.getMensajeErrorAutenticacion() + "', " + 
					"" + c.getDuracionTokenMilisegundos() + ", " + 
					"'" + c.getMensajeErrorInterno() + "', " + 
					"'" + c.getMensajeUsuarioDeshabilitado() + "', " + 
					"'" + c.getMensajeTokenExpirado() + "'" + 
				"); \n"); 
			} 
			//================================================>>>>>> 
			cadena.append("\n\n-- Usuario \n"); 
			
			List<Usuario> listaUsuario = sesion.createQuery( 
				"select c from Usuario c " 
			).list(); 
			
			for (Usuario c : listaUsuario) 
			{ 
				cadena.append("INSERT INTO usuario(" + 
					"id, " + 
					"telefono, " + 
					"password, " + 
					"rut, " + 
					"username, " + 
					"apellido_materno, " + 
					"apellido_paterno, " + 
					"visible, " + 
					"nombre, " + 
					"activo, " + 
					"largo_password" + 
				") " + 
				"VALUES (" + 
					"" + c.getId() + ", " + 
					"'" + c.getTelefono() + "', " + 
					"'" + c.getPassword() + "', " + 
					"'" + c.getRut() + "', " + 
					"'" + c.getUsername() + "', " + 
					"'" + c.getApellidoMaterno() + "', " + 
					"'" + c.getApellidoPaterno() + "', " + 
					"" + (c.getVisible() != null ? (c.getVisible() ? "true" : "false") : "null") + ", " + 
					"'" + c.getNombre() + "', " + 
					"" + (c.getActivo() != null ? (c.getActivo() ? "true" : "false") : "null") + ", " + 
					"" + c.getLargoPassword() + "" + 
				"); \n"); 
			} 
			//================================================>>>>>> 
			cadena.append("\n\n-- Cargo \n"); 
			
			List<Cargo> listaCargo = sesion.createQuery( 
				"select c from Cargo c " 
			).list(); 
			
			for (Cargo c : listaCargo) 
			{ 
				cadena.append("INSERT INTO cargo(" + 
					"id, " + 
					"visible, " + 
					"activo, " + 
					"descripcion, " + 
					"authority" + 
				") " + 
				"VALUES (" + 
					"" + c.getId() + ", " + 
					"" + (c.getVisible() != null ? (c.getVisible() ? "true" : "false") : "null") + ", " + 
					"" + (c.getActivo() != null ? (c.getActivo() ? "true" : "false") : "null") + ", " + 
					"'" + c.getDescripcion() + "', " + 
					"'" + c.getAuthority() + "'" + 
				"); \n"); 
			} 
			//================================================>>>>>> 
			cadena.append("\n\n-- UsuarioCargo \n"); 
			
			List<UsuarioCargo> listaUsuarioCargo = sesion.createQuery( 
				"select c from UsuarioCargo c " 
				+ "join fetch c.usuario "   // Obtiene informacion de la entidad padre Usuario 
				+ "join fetch c.cargo "   // Obtiene informacion de la entidad padre Cargo 
			).list(); 
			
			for (UsuarioCargo c : listaUsuarioCargo) 
			{ 
				cadena.append("INSERT INTO usuario_cargo(" + 
					"id, " + 
					"usuario_id, " + 
					"cargo_id" + 
				") " + 
				"VALUES (" + 
					"" + c.getId() + ", " + 
					"" + (c.getUsuario() != null ? (c.getUsuario().getId()) : "null") + ", " + 
					"" + (c.getCargo() != null ? (c.getCargo().getId()) : "null") + "" + 
				"); \n"); 
			} 
			//================================================>>>>>> 
			cadena.append("\n\n-- ContactoPersona \n"); 
			
			List<ContactoPersona> listaContactoPersona = sesion.createQuery( 
				"select c from ContactoPersona c " 
			).list(); 
			
			for (ContactoPersona c : listaContactoPersona) 
			{ 
				cadena.append("INSERT INTO contacto_persona(" + 
					"id, " + 
					"email, " + 
					"existe, " + 
					"ha_respondido, " + 
					"ultima_fecha_respuesta, " + 
					"ultima_fecha_envio_correo, " + 
					"nombre_completo" + 
				") " + 
				"VALUES (" + 
					"" + c.getId() + ", " + 
					"'" + c.getEmail() + "', " + 
					"" + (c.getExiste() != null ? (c.getExiste() ? "true" : "false") : "null") + ", " + 
					"" + (c.getHaRespondido() != null ? (c.getHaRespondido() ? "true" : "false") : "null") + ", " + 
					"" + (c.getUltimaFechaRespuesta() != null ? ("'" + new java.text.SimpleDateFormat("yyyy-MM-dd").format(c.getUltimaFechaRespuesta()) + "'") : "null") + ", " + 
					"" + (c.getUltimaFechaEnvioCorreo() != null ? ("'" + new java.text.SimpleDateFormat("yyyy-MM-dd").format(c.getUltimaFechaEnvioCorreo()) + "'") : "null") + ", " + 
					"'" + c.getNombreCompleto() + "'" + 
				"); \n"); 
			} 
			//================================================>>>>>> 
			cadena.append("\n\n-- EnvioMasivoCorreo \n"); 
			
			List<EnvioMasivoCorreo> listaEnvioMasivoCorreo = sesion.createQuery( 
				"select c from EnvioMasivoCorreo c " 
				+ "join fetch c.usuario "   // Obtiene informacion de la entidad padre Usuario 
			).list(); 
			
			for (EnvioMasivoCorreo c : listaEnvioMasivoCorreo) 
			{ 
				cadena.append("INSERT INTO envio_masivo_correo(" + 
					"id, " + 
					"estado, " + 
					"fecha_envio, " + 
					"contenido_correo, " + 
					"asunto_correo, " + 
					"email_origen_envio, " + 
					"usuario_id, " + 
					"descripcion" + 
				") " + 
				"VALUES (" + 
					"" + c.getId() + ", " + 
					"'" + c.getEstado() + "', " + 
					"" + (c.getFechaEnvio() != null ? ("'" + new java.text.SimpleDateFormat("yyyy-MM-dd").format(c.getFechaEnvio()) + "'") : "null") + ", " + 
					//"'" + c.getContenidoCorreo() + "', " + 
					"'---', " +                                    // ESTO LO CAMBIE
					"'" + c.getAsuntoCorreo() + "', " + 
					"'" + c.getEmailOrigenEnvio() + "', " + 
					"" + (c.getUsuario() != null ? (c.getUsuario().getId()) : "null") + ", " + 
					"'" + c.getDescripcion() + "'" + 
				"); \n"); 
			} 
			//================================================>>>>>> 
			cadena.append("\n\n-- EnvioCorreoContactoPersona \n"); 
			
			List<EnvioCorreoContactoPersona> listaEnvioCorreoContactoPersona = sesion.createQuery( 
				"select c from EnvioCorreoContactoPersona c " 
				+ "join fetch c.contactoPersona "   // Obtiene informacion de la entidad padre ContactoPersona 
				+ "join fetch c.envioMasivoCorreo "   // Obtiene informacion de la entidad padre EnvioMasivoCorreo 
			).list(); 
			
			for (EnvioCorreoContactoPersona c : listaEnvioCorreoContactoPersona) 
			{ 
				cadena.append("INSERT INTO envio_correo_contacto_persona(" + 
					"id, " + 
					"estado, " + 
					"contacto_persona_id, " + 
					"envio_masivo_correo_id" + 
				") " + 
				"VALUES (" + 
					"" + c.getId() + ", " + 
					"'" + c.getEstado() + "', " + 
					"" + (c.getContactoPersona() != null ? (c.getContactoPersona().getId()) : "null") + ", " + 
					"" + (c.getEnvioMasivoCorreo() != null ? (c.getEnvioMasivoCorreo().getId()) : "null") + "" + 
				"); \n"); 
			} 
			//================================================>>>>>> 
			cadena.append("\n\n-- MaestroEnvioEmail \n"); 
			
			List<MaestroEnvioEmail> listaMaestroEnvioEmail = sesion.createQuery( 
				"select c from MaestroEnvioEmail c " 
			).list(); 
			
			for (MaestroEnvioEmail c : listaMaestroEnvioEmail) 
			{ 
				cadena.append("INSERT INTO maestro_envio_email(" + 
					"id, " + 
					"puerto_email_envio, " + 
					"host_email_envio, " + 
					"password_email_envio, " + 
					"email_envio" + 
				") " + 
				"VALUES (" + 
					"" + c.getId() + ", " + 
					"'" + c.getPuertoEmailEnvio() + "', " + 
					"'" + c.getHostEmailEnvio() + "', " + 
					"'" + c.getPasswordEmailEnvio() + "', " + 
					"'" + c.getEmailEnvio() + "'" + 
				"); \n"); 
			} 
			//================================================>>>>>> 

			return cadena.toString(); 
		} 
		catch(Exception ex) 
		{ 
			throw new RuntimeException("Error al generar el respaldo." + ex.getMessage()); 
		} 
	} 
} 
