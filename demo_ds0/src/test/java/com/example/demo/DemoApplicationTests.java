package com.example.demo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ProjectService;
//import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
class DemoApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	//@Autowired
	//private TaskService taskService;


	@Before
	public void deleteAll() {
		System.out.println("Elimination of content on the repositories...");
		this.userRepository.deleteAll();
		this.projectRepository.deleteAll();
		this.taskRepository.deleteAll();
		System.out.println("Done.");
	}

	@Test
	void testUpdateUser() {
		//creati i due user
		User u1 =new User("mariorossi","password","Mario","Rossi");
		u1=userService.saveUser(u1);
		assertEquals(u1.getId().longValue(),1L);
		assertEquals(u1.getFirstName(),"Mario");

		User u2 =new User("lucaverdi","password","Luca","Verdi");
		u2=userService.saveUser(u2);
		assertEquals(u2.getId().longValue(),2L);
		assertEquals(u2.getFirstName(),"Luca");

		//aggiorno user1 con maria rossi
		User u1update=new User("mariarossi","password","Maria","Rossi");
		u1update.setId(u1.getId());
		u1update=userService.saveUser(u1update);
		assertEquals(u1update.getId().longValue(),1L);
		assertEquals(u1update.getFirstName(),"Maria");

		//proprietario di project 1 e 2 è user1update
		Project p1=new Project("Project1","This is a new project");
		p1.setOwner(u1update);
		p1=projectService.saveProject(p1);
		assertEquals(p1.getOwner(),u1update);
		assertEquals(p1.getName(),"Project1");

		Project p2=new Project("Project2","This is another new project");
		p2.setOwner(u1update);
		p2=projectService.saveProject(p2);
		assertEquals(p2.getOwner(),u1update);
		assertEquals(p2.getName(),"Project2");

		//ora sharare il project uno con membro un user2!
		p1=projectService.shareProjectWithUser(p1, u2);
		//verifichiamo i passi fatti
		
		//ricercati da ownerchiamato u1 update 
		List<Project> projectsOwner=projectRepository.findByOwner(u1update);
		assertEquals(projectsOwner.size(),2);
		assertEquals(projectsOwner.get(0),p1);
		assertEquals(projectsOwner.get(1),p2);

		//progetti ricercati con come member u2
		List<Project> projectsVisibleByUser2=projectRepository.findByMembers(u2);
		assertEquals(projectsVisibleByUser2.size(),1);
		assertEquals(projectsVisibleByUser2.get(0),p1);

		
		//user ricercati che sono membri di project 1 
		List<User> projectsfindByVisibleProjects=userRepository.findByVisibleProjects(p1);
		assertEquals(projectsfindByVisibleProjects.size(),1);
		assertEquals(projectsfindByVisibleProjects.get(0),u2);



	}

}
