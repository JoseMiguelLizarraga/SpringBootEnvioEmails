package com.proyecto.model.entity; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table; 


@Entity 
@Table(name="maestro_envio_email") 
public class MaestroEnvioEmail 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "puerto_email_envio") 
	private String puertoEmailEnvio; 

	@Column(name = "host_email_envio") 
	private String hostEmailEnvio; 

	@Column(name = "password_email_envio") 
	private String passwordEmailEnvio; 

	@Column(name = "email_envio") 
	private String emailEnvio; 

	public MaestroEnvioEmail() 
	{ 

	} 

	public MaestroEnvioEmail(String puertoEmailEnvio, String hostEmailEnvio, String passwordEmailEnvio, Integer id, String emailEnvio) 
	{ 
		this.puertoEmailEnvio = puertoEmailEnvio; 
		this.hostEmailEnvio = hostEmailEnvio; 
		this.passwordEmailEnvio = passwordEmailEnvio; 
		this.id = id; 
		this.emailEnvio = emailEnvio; 
	} 

	public String getPuertoEmailEnvio() { 
		return this.puertoEmailEnvio; 
	}  
	public void setPuertoEmailEnvio(String puertoEmailEnvio) { 
		this.puertoEmailEnvio = puertoEmailEnvio; 
	}  
	public String getHostEmailEnvio() { 
		return this.hostEmailEnvio; 
	}  
	public void setHostEmailEnvio(String hostEmailEnvio) { 
		this.hostEmailEnvio = hostEmailEnvio; 
	}  
	public String getPasswordEmailEnvio() { 
		return this.passwordEmailEnvio; 
	}  
	public void setPasswordEmailEnvio(String passwordEmailEnvio) { 
		this.passwordEmailEnvio = passwordEmailEnvio; 
	}  
	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
	public String getEmailEnvio() { 
		return this.emailEnvio; 
	}  
	public void setEmailEnvio(String emailEnvio) { 
		this.emailEnvio = emailEnvio; 
	}  
} 

