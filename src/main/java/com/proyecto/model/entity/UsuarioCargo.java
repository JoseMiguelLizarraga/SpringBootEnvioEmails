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
@Table(name="usuario_cargo") 
public class UsuarioCargo 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	//@JsonIgnore // Impedir problema generado al convertir a json la clase Cargo 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "cargo_id") 
	private Cargo cargo; 

	//@JsonIgnore // Impedir problema generado al convertir a json la clase Usuario 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "usuario_id") 
	private Usuario usuario; 

	public UsuarioCargo() 
	{ 

	} 

	public UsuarioCargo(Cargo cargo, Usuario usuario, Integer id) 
	{ 
		this.cargo = cargo; 
		this.usuario = usuario; 
		this.id = id; 
	} 

	public Cargo getCargo() { 
		return this.cargo; 
	}  
	public void setCargo(Cargo cargo) { 
		this.cargo = cargo; 
	}  
	public Usuario getUsuario() { 
		return this.usuario; 
	}  
	public void setUsuario(Usuario usuario) { 
		this.usuario = usuario; 
	}  
	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
} 

