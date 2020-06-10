package com.example.demo.validator;

import java.lang.String;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import com.example.demo.model.User;

@Component
public class userValidator implements Validator {
	final Integer MAX_NAME_LENGTH=100;
	final Integer MIN_NAME_LENGTH=2;

	@Override
	public void validate(Object o, Errors errors) {
		// TODO Auto-generated method stub
		User user=(User) o;
		String firstName=user.getFirstName().trim();
		String lastName=user.getLastName().trim();
		
		//controlla che i campi non siano vuoti o che superino min e max con
		if (firstName.trim().isEmpty()) errors.rejectValue("firstName","required");
		else if (firstName.length()< MIN_NAME_LENGTH || firstName.length()>MAX_NAME_LENGTH) errors.rejectValue("firstName","size");

		if (lastName.trim().isEmpty()) errors.rejectValue("lastName","required");
		else if (lastName.length()< MIN_NAME_LENGTH || lastName.length()>MAX_NAME_LENGTH) errors.rejectValue("lastName","size");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return User.class.equals(clazz);
	}

}
