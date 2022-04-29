package com.AppPI.AppPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.AppPI.AppPI.models.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	
	Usuario findById(long id);
	Usuario findByNome(String nome);
	
	// Query para a busca
	@Query(value = "select u from Usuario u where u.nome like %?1%")
	List<Usuario>findByNomes(String nome);

}
