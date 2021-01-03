package com.proyecto.model.entity; 

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.Column; 
import javax.persistence.FetchType; 
import javax.persistence.Table; 
import javax.persistence.ManyToOne; 
import javax.persistence.JoinColumn; 


@Entity 
@Table(name="envio_correo_contacto_persona") 
public class EnvioCorreoContactoPersona 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "estado") 
	private String estado; 

	//@JsonIgnore // Impedir problema generado al convertir a json la clase ContactoPersona 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "contacto_persona_id") 
	private ContactoPersona contactoPersona; 

	//@JsonIgnore // Impedir problema generado al convertir a json la clase EnvioMasivoCorreo 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "envio_masivo_correo_id") 
	private EnvioMasivoCorreo envioMasivoCorreo; 

	public EnvioCorreoContactoPersona() 
	{ 

	} 

	public EnvioCorreoContactoPersona(Integer id, String estado, ContactoPersona contactoPersona, EnvioMasivoCorreo envioMasivoCorreo) 
	{ 
		this.id = id; 
		this.estado = estado; 
		this.contactoPersona = contactoPersona; 
		this.envioMasivoCorreo = envioMasivoCorreo; 
	} 

	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
	public String getEstado() { 
		return this.estado; 
	}  
	public void setEstado(String estado) { 
		this.estado = estado; 
	}  
	public ContactoPersona getContactoPersona() { 
		return this.contactoPersona; 
	}  
	public void setContactoPersona(ContactoPersona contactoPersona) { 
		this.contactoPersona = contactoPersona; 
	}  
	public EnvioMasivoCorreo getEnvioMasivoCorreo() { 
		return this.envioMasivoCorreo; 
	}  
	public void setEnvioMasivoCorreo(EnvioMasivoCorreo envioMasivoCorreo) { 
		this.envioMasivoCorreo = envioMasivoCorreo; 
	}  
} 

