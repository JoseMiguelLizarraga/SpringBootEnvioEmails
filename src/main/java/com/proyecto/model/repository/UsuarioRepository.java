package com.proyecto.model.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.proyecto.model.entity.Usuario;


public interface UsuarioRepository extends CrudRepository<Usuario, Long>
{
	public Usuario findByUsername(String username);
	
    //=================================================>>>>
	// Busqueda con select 2
	
    @Query("select count(c.id) from Usuario c where (:#{#username} is null or c.username like %:#{#username}%)")
	long contarTotalRegistros_Select_2(@Param("username") String username);
	
	@Query("select c from Usuario c where (:#{#username} is null or c.username like %:#{#username}%)")
	List<Usuario> llenarSelect_2(@Param("username") String username, Pageable pageable);

	//=================================================>>>>
}
