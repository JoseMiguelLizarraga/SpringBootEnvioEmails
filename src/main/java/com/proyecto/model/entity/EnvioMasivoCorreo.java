package com.proyecto.model.entity; 

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.Column; 
import javax.persistence.FetchType; 
import javax.persistence.Table; 
import java.util.List; 
import java.util.ArrayList; 
import javax.persistence.OneToMany; 
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; 
import javax.persistence.ManyToOne; 
import javax.persistence.JoinColumn; 
import com.fasterxml.jackson.annotation.JsonFormat; 
import java.util.Date; 


@Entity 
@Table(name="envio_masivo_correo") 
public class EnvioMasivoCorreo 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "asunto_correo") 
	private String asuntoCorreo; 

	@Column(name = "email_origen_envio") 
	private String emailOrigenEnvio; 

	//@JsonIgnore // Impedir problema generado al convertir a json la clase Usuario 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "usuario_id") 
	private Usuario usuario; 

	@Column(name = "descripcion") 
	private String descripcion; 

	@Column(name = "estado") 
	private String estado; 

	@JsonFormat(pattern = "dd-MM-yyyy")  // Formatear fecha al convertir a json 
	@Column(name = "fecha_envio") 
	private Date fechaEnvio; 

	@Column(name = "contenido_correo") 
	private String contenidoCorreo; 

	@JsonIgnoreProperties({"envioMasivoCorreo"}) 
	@OneToMany(mappedBy="envioMasivoCorreo", fetch=FetchType.LAZY) 
	private List<EnvioCorreoContactoPersona> listaEnvioCorreoContactoPersona = new ArrayList<>(); 

	public EnvioMasivoCorreo() 
	{ 

	} 

	public EnvioMasivoCorreo(String asuntoCorreo, String emailOrigenEnvio, Usuario usuario, String descripcion, Integer id, String estado, Date fechaEnvio, String contenidoCorreo) 
	{ 
		this.asuntoCorreo = asuntoCorreo; 
		this.emailOrigenEnvio = emailOrigenEnvio; 
		this.usuario = usuario; 
		this.descripcion = descripcion; 
		this.id = id; 
		this.estado = estado; 
		this.fechaEnvio = fechaEnvio; 
		this.contenidoCorreo = contenidoCorreo; 
	} 

	public String getAsuntoCorreo() { 
		return this.asuntoCorreo; 
	}  
	public void setAsuntoCorreo(String asuntoCorreo) { 
		this.asuntoCorreo = asuntoCorreo; 
	}  
	public String getEmailOrigenEnvio() { 
		return this.emailOrigenEnvio; 
	}  
	public void setEmailOrigenEnvio(String emailOrigenEnvio) { 
		this.emailOrigenEnvio = emailOrigenEnvio; 
	}  
	public Usuario getUsuario() { 
		return this.usuario; 
	}  
	public void setUsuario(Usuario usuario) { 
		this.usuario = usuario; 
	}  
	public String getDescripcion() { 
		return this.descripcion; 
	}  
	public void setDescripcion(String descripcion) { 
		this.descripcion = descripcion; 
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
	public Date getFechaEnvio() { 
		return this.fechaEnvio; 
	}  
	public void setFechaEnvio(Date fechaEnvio) { 
		this.fechaEnvio = fechaEnvio; 
	}  
	public String getContenidoCorreo() { 
		return this.contenidoCorreo; 
	}  
	public void setContenidoCorreo(String contenidoCorreo) { 
		this.contenidoCorreo = contenidoCorreo; 
	}  
	public List<EnvioCorreoContactoPersona> getListaEnvioCorreoContactoPersona() { 
		return this.listaEnvioCorreoContactoPersona; 
	}  
	public void setListaEnvioCorreoContactoPersona(List<EnvioCorreoContactoPersona> listaEnvioCorreoContactoPersona) { 
		this.listaEnvioCorreoContactoPersona = listaEnvioCorreoContactoPersona; 
	}  
	
	public String toInsert_SQL() 
	{ 
		return "INSERT INTO envio_masivo_correo(" + 
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
			"" + this.id + ", " + 
			"'" + this.estado + "', " + 
			"" + (this.fechaEnvio != null ? ("'" + new java.text.SimpleDateFormat("yyyy-MM-dd").format(this.fechaEnvio) + "'") : "null") + ", " + 
			"'----', " +   //"'" + this.contenidoCorreo + "', " + 
			"'" + this.asuntoCorreo + "', " + 
			"'" + this.emailOrigenEnvio + "', " + 
			"" + (this.usuario != null ? (this.usuario.getId()) : "null") + ", " + 
			"'" + this.descripcion + "'" + 
		");"; 
	}  
} 

