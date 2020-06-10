package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Credentials;
import com.example.demo.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CredentialsRepository credentialsRepository;




	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result =this.credentialsRepository.findById(id);
		return result.orElse(null);
	}


	@Transactional
	public Credentials getCredentials(String username) {
		Optional<Credentials> result =this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}
	/*This method save an username with credentials in the DB
	 * @param= Credentials
	 * @return=credentials saved
	 * */
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
		//inizializzato ognuno come usern normale
		//save the credentials and the user associated
		//password nascosta.cifrata
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);	
	}
	

	@Transactional
	public List<Credentials> getAllCredentials(){
		Iterable<Credentials> itera=this.credentialsRepository.findAll();
		ArrayList<Credentials> lista=new ArrayList<>();
		for(Credentials c:itera) {
			lista.add(c);
		}
		return lista;
	}

	@Transactional
	public void deleteCredentials(String username) {

		Credentials credentials=this.getCredentials(username);
		this.credentialsRepository.delete(credentials);
	}



}
