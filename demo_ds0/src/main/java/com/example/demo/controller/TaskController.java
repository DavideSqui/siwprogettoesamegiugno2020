package com.example.demo.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.service.ProjectService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import com.example.demo.session.SessionData;
import com.example.demo.validator.taskValidator;

@Controller
public class TaskController {

	@Autowired
	private taskValidator taskValidator;

	@Autowired 
	private UserService userService;
	@Autowired 
	private ProjectService projectService;

	@Autowired
	private TaskService taskService;

	@Autowired 
	private SessionData sessionData;

	//-------------------------------------------------------------------------------------
	//NUOVI METODI PER AGGIUNGERE TASK
	//metodo che porta l'utente alla home


	@RequestMapping(value= {"/tasks/add"},method=RequestMethod.GET)
	public String createTaskForm(Model model) {

		User loggedUser=sessionData.getLoggedUser();

		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("projectForm",new Project());
		model.addAttribute("taskForm",new Task());
		return "addTask";
	}

	@Lazy(value=false)
	@RequestMapping(value= {"/tasks/add"},method=RequestMethod.POST)
	public String createTaskOperation(
			@Valid @ModelAttribute("projectForm") Project project,
			@Valid @ModelAttribute("taskForm") Task task,
			BindingResult taskBindingResult,
			Model model)	                  
	{	

		User loggedUser=sessionData.getLoggedUser();
		//Project loggedProject=sessionData.getLoggedProject();

		taskValidator.validate(task,taskBindingResult);

		if(!taskBindingResult.hasErrors()) {
			if(this.projectService.getProject(project.getId())!=null&&this.projectService.getProject(project.getId()).getOwner().equals(loggedUser)) {
				Project projectDefined=this.projectService.getProject(project.getId());
				task.setProject(projectDefined);
				//projectDefined.addTask(task);
				//loggedProject.getTaskList().add(task);
				//this.projectService.saveProject(loggedProject);
				//this.projectService.saveProject(projectDefined);
				this.taskService.saveTask(task);
				return "redirect:/projects/"+projectDefined.getId();
			}
		}
		model.addAttribute("loggedUser",loggedUser);
		//model.addAttribute("loggedProject",loggedProject);
		return "addTask";
	}
	//------------------------------------------------------------------------------------------------------
	/*METHODS UPDATE 
	 * OF THE TASKS
	 */

	@RequestMapping(value= {"/tasks/update"},method=RequestMethod.GET)
	public String modifyTaskForm(Model model) {

		User loggedUser=sessionData.getLoggedUser();

		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("projectForm",new Project());
		model.addAttribute("taskForm",new Task());
		return "modifyTask";
	}

	@Lazy(value=false)
	@RequestMapping(value= {"/tasks/update"},method=RequestMethod.POST)
	public String modifyTaskOperation(
			@Valid @ModelAttribute("projectForm") Project project,
			@Valid @ModelAttribute("taskForm") Task task,
			BindingResult taskBindingResult,
			Model model)	                  
	{	

		User loggedUser=sessionData.getLoggedUser();
		//Project loggedProject=sessionData.getLoggedProject();


		if(this.projectService.getProject(project.getId())!=null&&this.projectService.getProject(project.getId()).getOwner().equals(loggedUser)) {

			Project projectDefined=this.projectService.getProject(project.getId());
			Task taskDefined=this.taskService.getTask(task.getName());
			taskDefined.setName(task.getName());
			taskDefined.setDescription(task.getDescription());
			taskDefined.setCompleted(task.isCompleted());
			taskDefined.setProject(projectDefined);
			//projectDefined.addTask(task);
			//loggedProject.getTaskList().add(task);
			//this.projectService.saveProject(loggedProject);
			//this.projectService.saveProject(projectDefined);
			this.taskService.saveTask(taskDefined);
			return "modifyTaskPage";
		}

		model.addAttribute("loggedUser",loggedUser);
		//model.addAttribute("loggedProject",loggedProject);
		return "modifyTask";
	}

	//-----------------------------------------------------------------------------------
	/*METHODS
	 * OF
	 * DELETE TASK
	 */

	@RequestMapping(value= {"/tasks/delete"},method=RequestMethod.GET)
	public String deleteTask(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		model.addAttribute("user",loggedUser);
		model.addAttribute("projectForm",new Project());
		model.addAttribute("taskForm",new Task());
		return "taskDelete";
	}

	@RequestMapping(value= {"/tasks/delete"},method=RequestMethod.POST)
	public String deleteTaskSuccess(
			@Valid @ModelAttribute("projectForm") Project project,
			@Valid @ModelAttribute("taskForm") Task task,
			BindingResult projectBindingResult,Model model)
	{	
		User loggedUser=sessionData.getLoggedUser();
		Long id=project.getId();
		if(this.projectService.getProject(id)!=null&&this.projectService.getProject(id).getOwner().equals(loggedUser)) {	

			//Project projectDefined=this.projectService.getProject(project.getId());
			Task taskDefined=this.taskService.getTask(task.getName());
			this.taskService.deleteTask(taskDefined);
			return "deleteTaskPage";	
		}
		model.addAttribute("loggedUser",loggedUser);
		return "taskDelete";
	}

	//--------------------------------------------------------------------------
	/*METHOD ASSIGNMENT TO 
	 * USER TO TASK
	 */
	@RequestMapping(value= {"/tasks/assign"},method=RequestMethod.GET)
	public String addUsertoTask(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		model.addAttribute("user",loggedUser);
		model.addAttribute("projectForm",new Project());
		model.addAttribute("taskForm",new Task());
		model.addAttribute("userForm",new User());
		return "taskAssign";
	}

	@RequestMapping(value= {"/tasks/assign"},method=RequestMethod.POST)
	public String AddUserToTaskOperation(
			@Valid @ModelAttribute("projectForm") Project project,
			@Valid @ModelAttribute("taskForm") Task task,
			@Valid @ModelAttribute("userForm") User user,
			BindingResult taskBindingResult,
			Model model)	                  
	{	

		User loggedUser=sessionData.getLoggedUser();

		if(this.projectService.getProject(project.getId())!=null&&this.projectService.getProject(project.getId()).getOwner().equals(loggedUser)) {

			//Project projectDefined=this.projectService.getProject(project.getId());
			Task taskDefined=this.taskService.getTask(task.getName());
			User userAssigned=this.userService.getUser(user.getFirstName());
			//if(this.projectService.getProject(project.getId()).getVisibleUsers().get(userAssigned.getId().intValue())!=null) {
				userAssigned.setAssignedTask(taskDefined);

				//projectDefined.addTask(task);
				//loggedProject.getTaskList().add(task);
				//this.projectService.saveProject(loggedProject);
				//this.projectService.saveProject(projectDefined);
				userAssigned=this.userService.saveUser(userAssigned);
				//taskDefined=this.taskService.saveTask(taskDefined);
				return "TaskAssignPage";
			}
		//}
		model.addAttribute("loggedUser",loggedUser);
		return "taskAssign";
	}
}
