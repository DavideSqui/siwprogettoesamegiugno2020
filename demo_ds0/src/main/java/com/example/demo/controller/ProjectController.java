package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.model.Credentials;
import com.example.demo.model.Project;

import com.example.demo.model.User;
import com.example.demo.service.CredentialsService;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;
import com.example.demo.session.SessionData;
import com.example.demo.validator.projectValidator;

@Controller
public class ProjectController {

	@Autowired 
	private ProjectService projectService;

	@Autowired 
	private CredentialsService credentialsService;
	@Autowired 
	private UserService userService;

	@Autowired 
	private projectValidator projectValidator;

	@Autowired 
	private SessionData sessionData;

	//metodo che porta l'utente alla home
	@RequestMapping(value= {"/projects"},method=RequestMethod.GET)
	public String home(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		List<Project> projectsList=projectService.findByOwner(loggedUser);
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("projectsList",projectsList);
		return "MyOwnedProjects";
	}

	//metodo per permettere all'utente di vedere un progetto cosa ha dentro
	@RequestMapping(value= {"/projects/{projectId}"},method=RequestMethod.GET)
	public String project(Model model,@PathVariable Long projectId) {
		User loggedUser=this.sessionData.getLoggedUser();
		Project project=this.projectService.getProject(projectId);
		if(project==null)  return "redirect:/projects";

		List<User> members=this.userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser)&&!members.contains(loggedUser)) 
			return "redirect:/projects";

		//aggiungo gli attributi che utilizzerò per la visualizzazione
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("project",project);
		model.addAttribute("members",members);


		//ritornare alla pagina totale del progetto
		return "project";
	}

	//-----------------------------------------------------------------------------
	/*ADDERS 
	 * OF THE 
	 * PROJECTS*/
	//metodo che addda project
	@RequestMapping(value= {"/projects/add"},method=RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("projectForm",new Project());
		return "addProject";
	}


	@RequestMapping(value= {"/projects/add"},method=RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult,Model model)	                  
	{	
		User loggedUser=sessionData.getLoggedUser();
		projectValidator.validate(project,projectBindingResult);

		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/"+project.getId();
		}
		model.addAttribute("loggedUser",loggedUser);
		return "addProject";
	}

	//-------------------------------------------------------------------------------------
	/*MODIFIERS OF
	 * 
	 * THE PROJECTS*/
	@RequestMapping(value= {"/projects/modify"},method=RequestMethod.GET)
	public String modifyProjectDirection(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		model.addAttribute("user",loggedUser);
		model.addAttribute("projectForm",new Project());
		return "projectModify";
	}



	@RequestMapping(value= {"/projects/modify"},method=RequestMethod.POST)
	public String modifyProject(@Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult,Model model)
	{	
		User loggedUser=sessionData.getLoggedUser();


		this.projectValidator.validate(project,projectBindingResult);

		Long id=project.getId();
		if(!projectBindingResult.hasErrors()) {
			if(this.projectService.getProject(id)!=null&&this.projectService.getProject(id).getOwner().equals(loggedUser)) {
				Project projecto=this.projectService.getProject(project.getId());
				projecto.setName(project.getName());
				projecto.setDescription(project.getDescription());
				projecto.setOwner(loggedUser);
				this.projectService.saveProject(projecto);
				return "modifyProjectPage";
			}
		}
		return "projectModify";
	}


	//--------------------------------------------------------------------------------------
	/*SHARE
	 * 
	 * THE PROJECTS
	 * TO ANOTHER USER*/
	@RequestMapping(value= {"/projects/share"},method=RequestMethod.GET)
	public String shareProject(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		model.addAttribute("user",loggedUser);
		model.addAttribute("projectForm",new Project());
		model.addAttribute("credentialsForm",new Credentials());
		return "projectShare";
	}

	@RequestMapping(value= {"/projects/share"},method=RequestMethod.POST)
	public String modifyMyProject(
			@Valid @ModelAttribute("credentialsForm") Credentials credentials,
			@Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult,Model model)
	{	
		//Credentials loggedCredentials=sessionData.getLoggedCredentials();
		User loggedUser=sessionData.getLoggedUser();

		Long id=project.getId();
		if(this.projectService.getProject(id)!=null&&this.projectService.getProject(id).getOwner().equals(loggedUser)) {
			Project projecto=this.projectService.getProject(project.getId());
			String usedUsername=credentials.getUsername();
			if(this.credentialsService.getCredentials(usedUsername)!=null) {
				projecto=this.projectService.shareProjectWithUser(projecto, this.credentialsService.getCredentials(credentials.
						getUsername()).getUser());
				projecto.setOwner(loggedUser);
				this.projectService.saveProject(projecto);
				return "shareProjectPage";
			}
		}
		return "projectShare";
	}

	//------------------------------------------------------------------------------
	/*ELIMINATE
	 * 
	 * THE PROJECT
	 * */
	@RequestMapping(value= {"/projects/delete"},method=RequestMethod.GET)
	public String deleteProject(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		model.addAttribute("user",loggedUser);
		model.addAttribute("projectForm",new Project());
		return "projectDelete";
	}


	@RequestMapping(value= {"/projects/delete"},method=RequestMethod.POST)
	public String deleteProjectSuccess(@Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult,Model model)
	{	
			User loggedUser=sessionData.getLoggedUser();
		Long id=project.getId();
		if(this.projectService.getProject(id)!=null&&this.projectService.getProject(id).getOwner().equals(loggedUser)) {
			Project projecto=this.projectService.getProject(project.getId());
			this.projectService.deleteProject(projecto);
			return "deleteProjectPage";
		}
		return "projectDelete";
	}

	//------------------------------------------------------------------------------
	/*SEE
	 * 
	 * THE SHARED
	 * PROJECTS
	 * */

	@RequestMapping(value= {"/projects/sharing"},method=RequestMethod.GET)
	public String listOfSharedProject(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		List<Project> projectsList=this.projectService.findMembers(loggedUser);
		model.addAttribute("user",loggedUser);
		model.addAttribute("projectsList",projectsList);
		return "MyVisibleProjects";
	}

	//metodo per permettere all'utente di vedere un progetto condiviso cosa ha dentro
		@RequestMapping(value= {"/projects/sharing/{projectIp}"},method=RequestMethod.GET)
		public String sharedProject(Model model,@PathVariable Long projectIp) {
			User loggedUser=this.sessionData.getLoggedUser();
			Project project=this.projectService.getProject(projectIp);
			if(project==null)  return "redirect:/projects";

			List<User> members=this.userService.getMembers(project);
			if(!project.getOwner().equals(loggedUser)&&!members.contains(loggedUser)) 
				return "redirect:/projects";

			//aggiungo gli attributi che utilizzerò per la visualizzazione
			model.addAttribute("loggedUser",loggedUser);
			model.addAttribute("project",project);
			model.addAttribute("members",members);

		
		//	ritornare alla pagina totale del progetto
			return "projectVisible";
		}

}
