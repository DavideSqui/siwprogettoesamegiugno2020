package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Transactional
	public Project getProject(Long id) {
		Optional<Project> result =this.projectRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Project saveProject(Project project) {
		return this.projectRepository.save(project);
	}

	public void deleteProject(Project project) {
		this.projectRepository.delete(project);
	}



	@Transactional
	public Project shareProjectWithUser(Project project,User user) {	
		project.addMember(user);
		return this.projectRepository.save(project);
	}

	public List<Project> findByOwner(User u){
		Iterable<Project> itera=this.projectRepository.findAll();
		List<Project> lista=new ArrayList<>();
		for(Project p:itera) {
			if(p.getOwner().equals(u)) {
				lista.add(p);
			}
		}
		return lista;
	}

	
	
	
	public List<Project> findMembers(User u){
		List<Project> list=this.projectRepository.findByMembers(u);
		return list;
	}

}
