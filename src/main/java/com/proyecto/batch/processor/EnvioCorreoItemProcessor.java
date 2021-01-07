package com.proyecto.batch.processor;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.proyecto.dto.EnvioCorreoDTO;
import com.proyecto.model.entity.ContactoPersona;
import com.proyecto.model.entity.MaestroEnvioEmail;
import com.proyecto.model.repository.ContactoPersonaRepository;
import com.proyecto.model.repository.MaestroEnvioEmailRepository;


// Esto es un processor. Esto hace modificaciones a la informacion que esta procesando nuestro batch.
// implements ItemProcessor<Objeto de entrada, Objeto de salida>   // El objeto de salida podria ser de otro tipo

public class EnvioCorreoItemProcessor implements ItemProcessor<EnvioCorreoDTO, EnvioCorreoDTO>
{
	@Autowired private MaestroEnvioEmailRepository repositorio_MaestroEnvioEmail;	
	@Autowired private ContactoPersonaRepository repositorio_ContactoPersona;
	private static final Logger LOG = LoggerFactory.getLogger(EnvioCorreoItemProcessor.class);  // Esto es para poder escribir cosas en consola
	
	
	public void actualizarUltimaFechaEnvioCorreo(int idContactoPersona)
	{
		try {
			ContactoPersona contactoPersona = repositorio_ContactoPersona.findById(idContactoPersona).orElse(null);
			contactoPersona.setUltimaFechaEnvioCorreo(new Date());
			repositorio_ContactoPersona.save(contactoPersona);			
		} 
		catch (Exception ex) {
			LOG.info("SE ENCONTRÓ UN ERROR EN EL METODO actualizarUltimaFechaEnvioCorreo");
		}
	}

	@Override
	public EnvioCorreoDTO process(EnvioCorreoDTO datos) throws Exception 
	{
		/*
		-- En caso de tener que volver a enviar un correo que su envio fue fallido realizar esto:
			UPDATE envio_masivo_correo a, envio_correo_contacto_persona b
			SET a.estado = 'sin enviar', b.estado = 'sin enviar'
			WHERE b.envio_masivo_correo_id = a.id AND a.id = 48; 
		*/
		
		MaestroEnvioEmail maestroEnvioEmail = repositorio_MaestroEnvioEmail.findFirstByOrderByIdAsc(); 
		
		if (datos.getEstado().equals("sin enviar")) 
		{
			try {
				Properties prop = new Properties();
	
				prop.put("mail.smtp.host", maestroEnvioEmail.getHostEmailEnvio());    					//"smtp.gmail.com"
		        prop.put("mail.smtp.port", maestroEnvioEmail.getPuertoEmailEnvio());  					// "465"
		        prop.put("mail.smtp.auth", "true");
		        prop.put("mail.smtp.socketFactory.port", maestroEnvioEmail.getPuertoEmailEnvio());    	// "465"
		        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	
		        Session mailSession = Session.getDefaultInstance(prop);
		        //mailSession.setDebug(true);                            // Asi muestra los datos de envio del email en consola
		        Transport transport = mailSession.getTransport();
	
		        MimeMessage message = new MimeMessage(mailSession);
		        message.setSubject(datos.getAsuntoCorreo());
		        message.setContent(datos.getContenidoCorreo(), "text/html");    // message.setContent("<h1>Probando HTML tags</h1>", "text/html");
	
		        message.addRecipient(Message.RecipientType.TO, new InternetAddress(datos.getEmail()));
		        
		        transport.connect(
		        	maestroEnvioEmail.getHostEmailEnvio(),    					//"smtp.gmail.com"
		        	Integer.parseInt(maestroEnvioEmail.getPuertoEmailEnvio()),  // "465" 
		        	maestroEnvioEmail.getEmailEnvio(),        					//"tucorreo@gmail.com"
		        	maestroEnvioEmail.getPasswordEmailEnvio() 					//"tuclave"
		        );
	
		        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
		        transport.close();
		        
		        datos.setEstado("enviado"); 
		        actualizarUltimaFechaEnvioCorreo(datos.getIdContactoPersona());    
			} 
			catch (Exception e) 
			{
	        	LOG.info("SE ENCONTRÓ UN ERROR EN EL ENVIO DE MAIL A " + datos.getEmail());
				datos.setEstado("envio fallido");
			}
		}
		
		return datos;
	}

}






















