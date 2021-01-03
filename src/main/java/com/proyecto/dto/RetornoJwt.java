package com.proyecto.dto;

import java.util.Date;

public class RetornoJwt
{
	private String token;
	private String message;
	private Date expiration;
	private String emailEnvioCartas;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public String getEmailEnvioCartas() {
		return emailEnvioCartas;
	}
	public void setEmailEnvioCartas(String emailEnvioCartas) {
		this.emailEnvioCartas = emailEnvioCartas;
	}
}
