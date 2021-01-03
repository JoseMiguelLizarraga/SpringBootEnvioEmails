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


@Entity 
@Table(name="cargo") 
public class Cargo 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "authority") 
	private String authority; 

	@Column(name = "visible") 
	private Boolean visible; 

	@Column(name = "activo") 
	private Boolean activo; 

	@Column(name = "descripcion") 
	private String descripcion; 

	@JsonIgnoreProperties({"cargo"}) 
	@OneToMany(mappedBy="cargo", fetch=FetchType.LAZY) 
	private List<UsuarioCargo> listaUsuarioCargo = new ArrayList<>(); 

	public Cargo() 
	{ 

	} 

	public Cargo(String authority, Integer id, Boolean visible, Boolean activo, String descripcion) 
	{ 
		this.authority = authority; 
		this.id = id; 
		this.visible = visible; 
		this.activo = activo; 
		this.descripcion = descripcion; 
	} 

	public String getAuthority() { 
		return this.authority; 
	}  
	public void setAuthority(String authority) { 
		this.authority = authority; 
	}  
	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
	public Boolean getVisible() { 
		return this.visible; 
	}  
	public void setVisible(Boolean visible) { 
		this.visible = visible; 
	}  
	public Boolean getActivo() { 
		return this.activo; 
	}  
	public void setActivo(Boolean activo) { 
		this.activo = activo; 
	}  
	public String getDescripcion() { 
		return this.descripcion; 
	}  
	public void setDescripcion(String descripcion) { 
		this.descripcion = descripcion; 
	}  
	public List<UsuarioCargo> getListaUsuarioCargo() { 
		return this.listaUsuarioCargo; 
	}  
	public void setListaUsuarioCargo(List<UsuarioCargo> listaUsuarioCargo) { 
		this.listaUsuarioCargo = listaUsuarioCargo; 
	}  
} 

