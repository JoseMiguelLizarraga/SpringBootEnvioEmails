package com.proyecto.dto;

public class DetalleRespuestaApiValidarEmailDTO 
{
	private int status;
	private String log;
	
	public DetalleRespuestaApiValidarEmailDTO() {
	
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
}


//	"status": 1,
//	"log": "Success"

//==========================================>>>
/*
	"status":0,
	"log":"MailboxDoesNotExist"

*/

