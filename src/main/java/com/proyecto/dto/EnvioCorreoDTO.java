package com.proyecto.dto;

public class EnvioCorreoDTO 
{
	private Integer idEnvioCorreoContactoPersona; 
	private Integer idContactoPersona; 
	private String estado;
	private String email;
	private String asuntoCorreo;
	private String contenidoCorreo;
	

	public EnvioCorreoDTO() {

	}

	public Integer getIdEnvioCorreoContactoPersona() {
		return idEnvioCorreoContactoPersona;
	}

	public void setIdEnvioCorreoContactoPersona(Integer idEnvioCorreoContactoPersona) {
		this.idEnvioCorreoContactoPersona = idEnvioCorreoContactoPersona;
	}

	public Integer getIdContactoPersona() {
		return idContactoPersona;
	}

	public void setIdContactoPersona(Integer idContactoPersona) {
		this.idContactoPersona = idContactoPersona;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAsuntoCorreo() {
		return asuntoCorreo;
	}

	public void setAsuntoCorreo(String asuntoCorreo) {
		this.asuntoCorreo = asuntoCorreo;
	}

	public String getContenidoCorreo() {
		return contenidoCorreo;
	}

	public void setContenidoCorreo(String contenidoCorreo) {
		this.contenidoCorreo = contenidoCorreo;
	}

}
