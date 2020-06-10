package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Credentials;

@Repository
public interface CredentialsRepository extends CrudRepository<Credentials,Long> {

	/*metodo per cercare tramite l'username!
	 *  @param=username
	 *  @return=Optional with passed username
	 */
	public Optional<Credentials> findByUsername(String username);
	
	
}
