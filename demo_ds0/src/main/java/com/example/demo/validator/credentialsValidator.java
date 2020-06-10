package com.example.demo.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.model.Credentials;

import com.example.demo.service.CredentialsService;

@Component
public class credentialsValidator implements Validator {

	@Autowired 
	private CredentialsService credentialService;


	final Integer MAX_USERNAME_LENGTH=20;
	final Integer MIN_USERNAME_LENGTH=4;
	final Integer MAX_PASSWORD_LENGTH=20;
	final Integer MIN_PASSWORD_LENGTH=6;

	@Override
	public void validate(Object o, Errors errors) {
		// TODO Auto-generated method stub
		Credentials credentials=(Credentials) o;
		String username=credentials.getUsername().trim();
		String password=credentials.getPassword().trim();	

		if (username.trim().isEmpty()) errors.rejectValue("username","required");
		else if (username.length()< MIN_USERNAME_LENGTH || username.length()>MAX_USERNAME_LENGTH) errors.rejectValue("username","size");
		     else if (this.credentialService.getCredentials(username)!=null) errors.rejectValue("username","duplicate");


		if (password.trim().isEmpty()) errors.rejectValue("password","required");
		    else if (password.length()< MIN_PASSWORD_LENGTH || password.length()>MAX_PASSWORD_LENGTH) errors.rejectValue("password","size");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Credentials.class.equals(clazz);
	}
}
