package com.example.demo.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.model.Credentials;
import com.example.demo.model.User;
import com.example.demo.service.CredentialsService;
import com.example.demo.validator.userValidator;
import com.example.demo.validator.credentialsValidator;


/*This controller control the user authentication on the server.*/
//get e post gestite da AutenticationController
@Controller
public class AutenticationController {


	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private userValidator userValidator;

	@Autowired
	private credentialsValidator credentialsValidator;

	/*This method create the user connection to /register page
	 * so the get method relative at that href
	 * @param Model 
	 * @return String registerUser	 
	 */
	@RequestMapping(value= {"/users/register"},method=RequestMethod.GET)
	public String showRegisterForm(Model model) {
		model.addAttribute("userForm",new User());
		model.addAttribute("credentialsForm",new Credentials());
		//lista dei campi.
		return "registerUser";
	}

	@RequestMapping(value= {"/users/register"},method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("userForm") User user ,
			                   BindingResult userBindingResult,
			                   @Valid @ModelAttribute("credentialsForm") Credentials credentials,
			                   BindingResult credentialsBindingResult, Model model)
	{	
		this.userValidator.validate(user,userBindingResult);
		this.credentialsValidator.validate(credentials,credentialsBindingResult);
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			return "registrationSuccessful";
		}
		return "registerUser";
	}




}
