package com.AppPI.AppPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.AppPI.AppPI.models.Musica;
import com.AppPI.AppPI.models.Usuario;

public interface MusicaRepository extends CrudRepository<Musica, Long> {

	Iterable<Musica> findByUsuario(Usuario usuario);

	// para o método delete por id
	Musica findById(long id);
	List<Musica> findByNome(String nome);

	// Query para a busca
	@Query(value = "select u from Musica u where u.nome like %?1%")
	List<Musica> findByNomesMusicas(String nome);

}
