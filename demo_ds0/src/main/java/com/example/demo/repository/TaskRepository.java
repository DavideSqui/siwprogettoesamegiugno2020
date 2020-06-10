package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.example.demo.model.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task,Long>,JpaRepository<Task,Long>{

	public Optional<Task> findByName(String name);

	Long deleteByName(String name);
}
