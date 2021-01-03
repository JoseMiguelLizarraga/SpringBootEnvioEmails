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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.proyecto.DeserializarFechas;
import com.fasterxml.jackson.annotation.JsonFormat; 
import java.util.Date; 


@Entity 
@Table(name="contacto_persona") 
public class ContactoPersona 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "nombre_completo") 
	private String nombreCompleto; 

	@Column(name = "email") 
	private String email; 

	@Column(name = "existe") 
	private Boolean existe; 

	@Column(name = "ha_respondido") 
	private Boolean haRespondido; 

	@JsonFormat(pattern = "dd-MM-yyyy")  // Formatear fecha al convertir a json 
	@JsonDeserialize(using = DeserializarFechas.class)
	@Column(name = "ultima_fecha_respuesta") 
	private Date ultimaFechaRespuesta; 

	@JsonFormat(pattern = "dd-MM-yyyy")  // Formatear fecha al convertir a json 
	@JsonDeserialize(using = DeserializarFechas.class)
	@Column(name = "ultima_fecha_envio_correo") 
	private Date ultimaFechaEnvioCorreo; 

	@JsonIgnoreProperties({"contactoPersona"}) 
	@OneToMany(mappedBy="contactoPersona", fetch=FetchType.LAZY) 
	private List<EnvioCorreoContactoPersona> listaEnvioCorreoContactoPersona = new ArrayList<>(); 

	public ContactoPersona() 
	{ 

	} 

	public ContactoPersona(String nombreCompleto, String email, Boolean existe, Boolean haRespondido, Date ultimaFechaRespuesta, Integer id, Date ultimaFechaEnvioCorreo) 
	{ 
		this.nombreCompleto = nombreCompleto; 
		this.email = email; 
		this.existe = existe; 
		this.haRespondido = haRespondido; 
		this.ultimaFechaRespuesta = ultimaFechaRespuesta; 
		this.id = id; 
		this.ultimaFechaEnvioCorreo = ultimaFechaEnvioCorreo; 
	} 

	public String getNombreCompleto() { 
		return this.nombreCompleto; 
	}  
	public void setNombreCompleto(String nombreCompleto) { 
		this.nombreCompleto = nombreCompleto; 
	}  
	public String getEmail() { 
		return this.email; 
	}  
	public void setEmail(String email) { 
		this.email = email; 
	}  
	public Boolean getExiste() { 
		return this.existe; 
	}  
	public void setExiste(Boolean existe) { 
		this.existe = existe; 
	}  
	public Boolean getHaRespondido() { 
		return this.haRespondido; 
	}  
	public void setHaRespondido(Boolean haRespondido) { 
		this.haRespondido = haRespondido; 
	}  
	public Date getUltimaFechaRespuesta() { 
		return this.ultimaFechaRespuesta; 
	}  
	public void setUltimaFechaRespuesta(Date ultimaFechaRespuesta) { 
		this.ultimaFechaRespuesta = ultimaFechaRespuesta; 
	}  
	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
	public Date getUltimaFechaEnvioCorreo() { 
		return this.ultimaFechaEnvioCorreo; 
	}  
	public void setUltimaFechaEnvioCorreo(Date ultimaFechaEnvioCorreo) { 
		this.ultimaFechaEnvioCorreo = ultimaFechaEnvioCorreo; 
	}  
	public List<EnvioCorreoContactoPersona> getListaEnvioCorreoContactoPersona() { 
		return this.listaEnvioCorreoContactoPersona; 
	}  
	public void setListaEnvioCorreoContactoPersona(List<EnvioCorreoContactoPersona> listaEnvioCorreoContactoPersona) { 
		this.listaEnvioCorreoContactoPersona = listaEnvioCorreoContactoPersona; 
	}  
	
	public String toInsert_SQL() 
	{ 
		return "INSERT INTO contacto_persona(" + 
			"id, " + 
			"email, " + 
			"existe, " + 
			"ha_respondido, " + 
			"ultima_fecha_respuesta, " + 
			"ultima_fecha_envio_correo, " + 
			"nombre_completo" + 
		") " + 
		"VALUES (" + 
			"" + this.id + ", " + 
			"'" + this.email + "', " + 
			"" + (this.existe != null ? (this.existe ? "true" : "false") : "null") + ", " + 
			"" + (this.haRespondido != null ? (this.haRespondido ? "true" : "false") : "null") + ", " + 
			"" + (this.ultimaFechaRespuesta != null ? ("'" + new java.text.SimpleDateFormat("yyyy-MM-dd").format(this.ultimaFechaRespuesta) + "'") : "null") + ", " + 
			"" + (this.ultimaFechaEnvioCorreo != null ? ("'" + new java.text.SimpleDateFormat("yyyy-MM-dd").format(this.ultimaFechaEnvioCorreo) + "'") : "null") + ", " + 
			"'" + this.nombreCompleto + "'" + 
		");"; 
	}  
} 

