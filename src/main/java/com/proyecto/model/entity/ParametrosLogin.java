package com.proyecto.model.entity; 

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.Column; 
import javax.persistence.FetchType; 
import javax.persistence.Table; 


@Entity 
@Table(name="parametros_login") 
public class ParametrosLogin 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "mensaje_token_expirado") 
	private String mensajeTokenExpirado; 

	@Column(name = "mensaje_error_autenticacion") 
	private String mensajeErrorAutenticacion; 

	@Column(name = "duracion_token_milisegundos") 
	private Integer duracionTokenMilisegundos; 

	@Column(name = "mensaje_error_interno") 
	private String mensajeErrorInterno; 

	@Column(name = "mensaje_usuario_deshabilitado") 
	private String mensajeUsuarioDeshabilitado; 

	public ParametrosLogin() 
	{ 

	} 

	public ParametrosLogin(String mensajeTokenExpirado, String mensajeErrorAutenticacion, Integer duracionTokenMilisegundos, String mensajeErrorInterno, Integer id, String mensajeUsuarioDeshabilitado) 
	{ 
		this.mensajeTokenExpirado = mensajeTokenExpirado; 
		this.mensajeErrorAutenticacion = mensajeErrorAutenticacion; 
		this.duracionTokenMilisegundos = duracionTokenMilisegundos; 
		this.mensajeErrorInterno = mensajeErrorInterno; 
		this.id = id; 
		this.mensajeUsuarioDeshabilitado = mensajeUsuarioDeshabilitado; 
	} 

	public String getMensajeTokenExpirado() { 
		return this.mensajeTokenExpirado; 
	}  
	public void setMensajeTokenExpirado(String mensajeTokenExpirado) { 
		this.mensajeTokenExpirado = mensajeTokenExpirado; 
	}  
	public String getMensajeErrorAutenticacion() { 
		return this.mensajeErrorAutenticacion; 
	}  
	public void setMensajeErrorAutenticacion(String mensajeErrorAutenticacion) { 
		this.mensajeErrorAutenticacion = mensajeErrorAutenticacion; 
	}  
	public Integer getDuracionTokenMilisegundos() { 
		return this.duracionTokenMilisegundos; 
	}  
	public void setDuracionTokenMilisegundos(Integer duracionTokenMilisegundos) { 
		this.duracionTokenMilisegundos = duracionTokenMilisegundos; 
	}  
	public String getMensajeErrorInterno() { 
		return this.mensajeErrorInterno; 
	}  
	public void setMensajeErrorInterno(String mensajeErrorInterno) { 
		this.mensajeErrorInterno = mensajeErrorInterno; 
	}  
	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
	public String getMensajeUsuarioDeshabilitado() { 
		return this.mensajeUsuarioDeshabilitado; 
	}  
	public void setMensajeUsuarioDeshabilitado(String mensajeUsuarioDeshabilitado) { 
		this.mensajeUsuarioDeshabilitado = mensajeUsuarioDeshabilitado; 
	}  
} 

