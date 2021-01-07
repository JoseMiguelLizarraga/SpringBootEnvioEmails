package com.proyecto.model.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.proyecto.model.entity.EnvioMasivoCorreo;


public interface EnvioMasivoCorreoRepository extends CrudRepository<EnvioMasivoCorreo, Integer>
{
	
	@Query(
		"select c from EnvioMasivoCorreo c " + 
		"join fetch c.usuario " +   // Obtiene informacion de la entidad padre Usuario 
		"where c.id = :id"
	)
	public EnvioMasivoCorreo buscarPorId(@Param("id") int id);
	
	
	@Query( 
		"select new EnvioMasivoCorreo("  // Utiliza el constructor de la entidad EnvioMasivoCorreo con todos los argumentos
			+ "c.asuntoCorreo, "
			+ "c.emailOrigenEnvio, "
			+ "u as usuario, "
			+ "c.descripcion, "
			+ "c.id, "
			+ "c.estado, "
			+ "c.fechaEnvio, "
			+ "c.contenidoCorreo"
		+ ") "
		+ "from EnvioMasivoCorreo c " 
		+ "inner join Usuario u on c.usuario.id = u.id "   // Obtiene informacion de la entidad padre Usuario
	)
	public List<EnvioMasivoCorreo> findAll();
	
	//================================================================>>>>>
	
	public String consultaTablaBusquedas = "where "
		+ "(:asuntoCorreo is null or c.asuntoCorreo like %:asuntoCorreo%) and "
		+ "(:emailOrigenEnvio is null or c.emailOrigenEnvio like %:emailOrigenEnvio%) and "
		+ "(:idUsuario = 0 or c.usuario.id = :idUsuario) and "
		+ "(:descripcion is null or c.descripcion like %:descripcion%) and "
		+ "(:estado is null or c.estado like %:estado%) and "		
		// + "c.fechaEnvio >= coalesce(:fechaEnvioDesde, c.fechaEnvio) and "                      // FUNCIONA SOLO EN MYSQL
		+ "(cast(:fechaEnvioDesde as date) is null or c.fechaEnvio >= :fechaEnvioDesde) and "     
		+ "(cast(:fechaEnvioHasta as date) is null or c.fechaEnvio <= :fechaEnvioHasta) ";     
 		
	@Query("select count(c.id) from EnvioMasivoCorreo c " + consultaTablaBusquedas)
	long contarTotalRegistrosDataTable(
		@Param("asuntoCorreo") String asuntoCorreo,
		@Param("emailOrigenEnvio") String emailOrigenEnvio,
		@Param("idUsuario") int idUsuario,
		@Param("descripcion") String descripcion,
		@Param("estado") String estado,
		@Param("fechaEnvioDesde") Date fechaEnvioDesde,
		@Param("fechaEnvioHasta") Date fechaEnvioHasta
	);
	
	
	@Query( 
		"select new EnvioMasivoCorreo("  // Utiliza el constructor de la entidad EnvioMasivoCorreo con todos los argumentos
			+ "c.asuntoCorreo, "
			+ "c.emailOrigenEnvio, "
			+ "usuario, "
			+ "c.descripcion, "
			+ "c.id, "
			+ "c.estado, "
			+ "c.fechaEnvio, "
			+ "c.contenidoCorreo"
		+ ") "
		+ "from EnvioMasivoCorreo c " 
		+ "inner join Usuario usuario on c.usuario.id = usuario.id "   // Obtiene informacion de la entidad padre Usuario
		+ consultaTablaBusquedas 
		+ "order by c.id desc"
	)
	List<EnvioMasivoCorreo> llenarDataTable(
		@Param("asuntoCorreo") String asuntoCorreo,
		@Param("emailOrigenEnvio") String emailOrigenEnvio,
		@Param("idUsuario") int idUsuario,
		@Param("descripcion") String descripcion,
		@Param("estado") String estado,
		@Param("fechaEnvioDesde") Date fechaEnvioDesde,
		@Param("fechaEnvioHasta") Date fechaEnvioHasta,
		Pageable pageable
	);
	
	//================================================================>>>>>
	
	@Query("select c from EnvioMasivoCorreo c where c.estado = :estado")  
	public List<EnvioMasivoCorreo> findAllByEstado(@Param("estado") String estado);

}






















