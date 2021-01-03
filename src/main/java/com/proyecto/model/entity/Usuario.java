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
import org.hibernate.annotations.Fetch; 
import org.hibernate.annotations.FetchMode; 


@Entity 
@Table(name="usuario") 
public class Usuario 
{ 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Integer id; 

	@Column(name = "visible") 
	private Boolean visible; 

	@Column(name = "nombre") 
	private String nombre; 

	@Column(name = "activo") 
	private Boolean activo; 

	@Column(name = "largo_password") 
	private Integer largoPassword; 

	@Column(name = "telefono") 
	private String telefono; 

	@Column(name = "password") 
	private String password; 

	@Column(name = "rut") 
	private String rut; 

	@Column(name = "username") 
	private String username; 

	@Column(name = "apellido_materno") 
	private String apellidoMaterno; 

	@Column(name = "apellido_paterno") 
	private String apellidoPaterno; 

	@JsonIgnoreProperties({"usuario"}) 
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY) 
	@Fetch(value = FetchMode.SUBSELECT) 
	private List<EnvioMasivoCorreo> listaEnvioMasivoCorreo = new ArrayList<>(); 

	@JsonIgnoreProperties({"usuario"}) 
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY) 
	@Fetch(value = FetchMode.SUBSELECT) 
	private List<UsuarioCargo> listaUsuarioCargo = new ArrayList<>(); 

	public Usuario() 
	{ 

	} 

	public Usuario(Boolean visible, String nombre, Boolean activo, Integer largoPassword, String telefono, String password, String rut, String username, Integer id, String apellidoMaterno, String apellidoPaterno) 
	{ 
		this.visible = visible; 
		this.nombre = nombre; 
		this.activo = activo; 
		this.largoPassword = largoPassword; 
		this.telefono = telefono; 
		this.password = password; 
		this.rut = rut; 
		this.username = username; 
		this.id = id; 
		this.apellidoMaterno = apellidoMaterno; 
		this.apellidoPaterno = apellidoPaterno; 
	} 

	public Boolean getVisible() { 
		return this.visible; 
	}  
	public void setVisible(Boolean visible) { 
		this.visible = visible; 
	}  
	public String getNombre() { 
		return this.nombre; 
	}  
	public void setNombre(String nombre) { 
		this.nombre = nombre; 
	}  
	public Boolean getActivo() { 
		return this.activo; 
	}  
	public void setActivo(Boolean activo) { 
		this.activo = activo; 
	}  
	public Integer getLargoPassword() { 
		return this.largoPassword; 
	}  
	public void setLargoPassword(Integer largoPassword) { 
		this.largoPassword = largoPassword; 
	}  
	public String getTelefono() { 
		return this.telefono; 
	}  
	public void setTelefono(String telefono) { 
		this.telefono = telefono; 
	}  
	public String getPassword() { 
		return this.password; 
	}  
	public void setPassword(String password) { 
		this.password = password; 
	}  
	public String getRut() { 
		return this.rut; 
	}  
	public void setRut(String rut) { 
		this.rut = rut; 
	}  
	public String getUsername() { 
		return this.username; 
	}  
	public void setUsername(String username) { 
		this.username = username; 
	}  
	public Integer getId() { 
		return this.id; 
	}  
	public void setId(Integer id) { 
		this.id = id; 
	}  
	public String getApellidoMaterno() { 
		return this.apellidoMaterno; 
	}  
	public void setApellidoMaterno(String apellidoMaterno) { 
		this.apellidoMaterno = apellidoMaterno; 
	}  
	public String getApellidoPaterno() { 
		return this.apellidoPaterno; 
	}  
	public void setApellidoPaterno(String apellidoPaterno) { 
		this.apellidoPaterno = apellidoPaterno; 
	}  
	public List<EnvioMasivoCorreo> getListaEnvioMasivoCorreo() { 
		return this.listaEnvioMasivoCorreo; 
	}  
	public void setListaEnvioMasivoCorreo(List<EnvioMasivoCorreo> listaEnvioMasivoCorreo) { 
		this.listaEnvioMasivoCorreo = listaEnvioMasivoCorreo; 
	}  
	public List<UsuarioCargo> getListaUsuarioCargo() { 
		return this.listaUsuarioCargo; 
	}  
	public void setListaUsuarioCargo(List<UsuarioCargo> listaUsuarioCargo) { 
		this.listaUsuarioCargo = listaUsuarioCargo; 
	}  
} 

