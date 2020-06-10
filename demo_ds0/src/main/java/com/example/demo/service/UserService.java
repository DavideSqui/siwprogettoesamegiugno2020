package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired 
	private UserRepository userRepository;

	@Transactional
	public User getUser(Long id) {
		Optional<User> result =this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public User getUser(String firstName) {
		Optional<User> result =this.userRepository.findByFirstName(firstName);
		return result.orElse(null);
	}



	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}

	@Transactional
	public List<User> findAllUsers(){
		Iterable<User> itera=this.userRepository.findAll();
		ArrayList<User> lista=new ArrayList<>();
		for(User u:itera) {
			lista.add(u);
		}
		return lista;
	}

	//metodo per ottenere tutti i membri del progetto
	@Transactional
	public List<User> getMembers(Project project){return this.userRepository.findByVisibleProjects(project);	}


}
