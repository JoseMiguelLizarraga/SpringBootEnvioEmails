package com.proyecto.dto;

public class RespuestaApiValidarEmailDTO 
{
	private String email;
	private DetalleRespuestaApiValidarEmailDTO response;
	private int credits;
	
	
	public RespuestaApiValidarEmailDTO() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DetalleRespuestaApiValidarEmailDTO getResponse() {
		return response;
	}

	public void setResponse(DetalleRespuestaApiValidarEmailDTO response) {
		this.response = response;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}
	
}

