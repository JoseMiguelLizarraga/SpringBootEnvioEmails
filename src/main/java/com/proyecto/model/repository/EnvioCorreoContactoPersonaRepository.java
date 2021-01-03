package com.proyecto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.proyecto.model.entity.ContactoPersona;
import com.proyecto.model.entity.EnvioCorreoContactoPersona;
import com.proyecto.model.entity.EnvioMasivoCorreo;

public interface EnvioCorreoContactoPersonaRepository extends CrudRepository<EnvioCorreoContactoPersona, Integer>
{
	
 	@Query( 
	"select new EnvioCorreoContactoPersona("  // Utiliza el constructor de la entidad EnvioCorreoContactoPersona con todos los argumentos
		+ "c.id, "
		+ "c.estado, "
		+ "contactoPersona, "
		+ "envioMasivoCorreo"
	+ ") "
	+ "from EnvioCorreoContactoPersona c " 
	+ "inner join ContactoPersona contactoPersona on c.contactoPersona.id = contactoPersona.id "   		   // Obtiene informacion de la entidad padre ContactoPersona
	+ "inner join EnvioMasivoCorreo envioMasivoCorreo on c.envioMasivoCorreo.id = envioMasivoCorreo.id "   // Obtiene informacion de la entidad padre ContactoPersona
 	)
	public List<EnvioCorreoContactoPersona> findAll();
 	
	//===========================================>>>>
	
	public boolean existsByEnvioMasivoCorreo(EnvioMasivoCorreo envioMasivoCorreo);
	
	
	// delete from EnvioCorreoContactoPersona where contactoPersona.id = 1
	public void deleteByContactoPersona(ContactoPersona contactoPersona);  // Borrar los elementos de tipo EnvioCorreoContactoPersona que pertenecen a la entidad ContactoPersona

	//============================================>>>>
	// Borrar los elementos de tipo EnvioCorreoContactoPersona que pertenecen a la entidad EnvioMasivoCorreo 
	
	// delete from EnvioCorreoContactoPersona where envioMasivoCorreo.id = 1
	//public void deleteByEnvioMasivoCorreo(EnvioMasivoCorreo envioMasivoCorreo);           
	
	@Modifying
	@Query("delete from EnvioCorreoContactoPersona where envioMasivoCorreo.id = :id")
	void deleteByEnvioMasivoCorreoId(@Param("id") int id);
	
	//============================================>>>>

	// Encontrar los hijos de la entidad EnvioMasivoCorreo
	@Query(
		"select c from EnvioCorreoContactoPersona c " 
		+ "join fetch c.contactoPersona "   // Obtiene informacion de la entidad padre ContactoPersona 
		+ "where c.envioMasivoCorreo = :envioMasivoCorreo"
	)
	public List<EnvioCorreoContactoPersona> findAllByEnvioMasivoCorreo(@Param("envioMasivoCorreo") EnvioMasivoCorreo envioMasivoCorreo);  // Buscar los elementos de tipo EnvioCorreoContactoPersona que pertenecen a la entidad EnvioMasivoCorreo

	
	@Modifying
	@Query("delete from EnvioCorreoContactoPersona where envioMasivoCorreo.id = :envioMasivoCorreoId and id not in :ids")
	void deleteByEnvioMasivoCorreoAndIds(
		@Param("envioMasivoCorreoId") int envioMasivoCorreoId,
		@Param("ids") List<Integer> ids
	);
	

//	@Query("select count(c.id) from EnvioCorreoContactoPersona c where c.envioMasivoCorreo.id = :envioMasivoCorreoId and c.estado != :estado")
//	long countByEnvioMasivoCorreoIdAndEstadoNot(@Param("envioMasivoCorreoId") int envioMasivoCorreoId, @Param("estado") String estado);
	
	public boolean existsByEnvioMasivoCorreoAndEstadoNot(EnvioMasivoCorreo envioMasivoCorreo, String estado);

}
