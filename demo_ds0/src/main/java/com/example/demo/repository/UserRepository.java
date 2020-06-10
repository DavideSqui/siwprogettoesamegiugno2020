package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Project;
import com.example.demo.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

	public Optional<User> findByFirstName(String firstName);

	public List<User> findByVisibleProjects(Project project);

}
