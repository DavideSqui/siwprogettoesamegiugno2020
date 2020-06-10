package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.ProjectService;
import com.example.demo.service.TaskService;

@SpringBootTest
@RunWith(SpringRunner.class)
class TaskControllerTest {
	
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskService taskService;
	
	@Before
	public void deleteAll() {
		System.out.println("Elimination of content on the repository...");
		this.taskRepository.deleteAll();
		System.out.println("Done.");
	}
	@Test
	void testUpdateandDeleteTask() {
		Project p1=new Project("p1","p1");
		
		//Inserimento semplioce e salvataggio di project e task relativa
		Task t1 =new Task("task1","task1",p1);
		p1.addTask(t1);
		p1=this.projectService.saveProject(p1);
		
		//t1=this.taskService.saveTask(t1); questa si può anche omettere
		
		assertEquals(p1.getId(),1L);
		assertEquals(t1.getId().longValue(),2L);
		assertEquals(t1.getName(),"task1");
		
		assertNotNull(this.taskService.getTask(t1.getId()));
		assertEquals(this.taskService.getTask(t1.getId()),t1);
		
	}

}
