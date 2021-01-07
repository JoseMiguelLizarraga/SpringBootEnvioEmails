package com.proyecto.model.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.proyecto.model.entity.ContactoPersona;

public interface ContactoPersonaRepository extends CrudRepository<ContactoPersona, Integer> 
{  
	//public ContactoPersona findById(@Param("id") int id);
	
    boolean existsByEmail(String email);
    
   
    @Query("select count(c) > 0 from ContactoPersona c where c.email = :email and (:id is null or c.id <> :id)")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Integer id);
    
    
    public List<ContactoPersona> findAll();
    
    //=================================================>>>>

    @Query("select count(c.id) from ContactoPersona c where (:email is null or c.email like %:email%) and existe = :existe")
	long contarTotalRegistros_Select_2(@Param("email") String email, Boolean existe);
	
	@Query(
		"select c from ContactoPersona c where (:email is null or c.email like %:email%) and existe = :existe " +
		"order by " +
			"c.ultimaFechaEnvioCorreo nulls first, " + 
			"ultimaFechaEnvioCorreo asc"
	)
	List<ContactoPersona> llenarSelect_2(@Param("email") String email, Boolean existe,  Pageable pageable);

	//=================================================>>>>
	
    @Modifying
    @Query("update ContactoPersona c SET c.existe = true WHERE c.email = :email")
    public void actualizarEmailValidado(@Param("email") String email);
}
