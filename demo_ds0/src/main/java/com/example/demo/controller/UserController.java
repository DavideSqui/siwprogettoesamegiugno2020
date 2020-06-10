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
import com.example.demo.model.User;
import com.example.demo.service.CredentialsService;
import com.example.demo.session.SessionData;
import com.example.demo.validator.credentialsValidator;
import com.example.demo.validator.userValidator;

@Controller
public class UserController {
	@Autowired
	private SessionData sessionData;

	@Autowired 
	private CredentialsService credentialsService;
	
	//nuovi:
	@Autowired
	private userValidator userValidator;

	@Autowired
	private credentialsValidator credentialsValidator;


	//metodo che porta l'utente alla home
	@RequestMapping(value= {"/home"},method=RequestMethod.GET)
	public String home(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		model.addAttribute("user",loggedUser);
		return "home";
	}

	//metodo che porta l'utente alla home
	@RequestMapping(value= {"/admin"},method=RequestMethod.GET)
	public String admin(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		model.addAttribute("user",loggedUser);
		return "admin";

	}

	//intercetta le richieste get a get user e restituisce le componenti di user e credenziali a /user/me
	@RequestMapping(value= {"/users/me"},method=RequestMethod.GET)
	public String me(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		Credentials credentials=sessionData.getLoggedCredentials();
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("credentials",credentials);
		
		return "userProfile";
	}


	/*METODI
	 * DELL'ADMIN
	 */
	@RequestMapping(value= {"/admin/users"},method=RequestMethod.GET)
	public String userList(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		List<Credentials> allCredentials=this.credentialsService.getAllCredentials();

		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("credentialsList",allCredentials);

		return "allUsers";	
	}

	@RequestMapping(value= {"/admin/users/{username}/delete"},method=RequestMethod.GET)
	public String removeuser(Model model,@PathVariable String username) {
		this.credentialsService.deleteCredentials(username);
		return "redirect:/admin/users";
	}

	/*Metodi
	 * AGGIUNTI 
	 * ORA
	 */


	@RequestMapping(value= {"/users/me/modify"},method=RequestMethod.GET)
	public String modifyMe(Model model) {
		User loggedUser=sessionData.getLoggedUser();
		Credentials credentials=sessionData.getLoggedCredentials();
		model.addAttribute("user",loggedUser);
		model.addAttribute("credentials",credentials);
		model.addAttribute("userForm",new User());
		model.addAttribute("credentialsForm",new Credentials());
		return "userProfileModify";
	}

	@RequestMapping(value= {"/users/me/modify"},method=RequestMethod.POST)
	public String modifyUser(@Valid @ModelAttribute("userForm") User user ,
			                   BindingResult userBindingResult,
			                   @Valid @ModelAttribute("credentialsForm") Credentials credentials,
			                   BindingResult credentialsBindingResult, Model model)
	{	
		User loggedUser=sessionData.getLoggedUser();
		Credentials credential=sessionData.getLoggedCredentials();
		
		this.userValidator.validate(user,userBindingResult);
		this.credentialsValidator.validate(credentials,credentialsBindingResult);
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			//credentials.setUser(user);
			credential.setUsername(credentials.getUsername());
			credential.setPassword(credentials.getPassword());
			credential.setRole(credentials.getRole());
			loggedUser.setFirstName(user.getFirstName());
			loggedUser.setLastName(user.getLastName());
			loggedUser.setOwnedProjects(user.getOwnedProjects());
			loggedUser.setOwnedProjects(user.getVisibleProjects());
			credential.setUser(loggedUser);
			this.credentialsService.saveCredentials(credential);
			return "modifyUserPage";
		}
		return "userProfileModify";
	}

	
	


}
