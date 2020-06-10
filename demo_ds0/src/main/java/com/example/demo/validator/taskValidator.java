package com.example.demo.validator;



import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import com.example.demo.model.Task;


@Component
public class taskValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Task.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		// TODO Auto-generated method stub
		
		final Integer MAX_NAME_LENGTH=20;
		final Integer MIN_NAME_LENGTH=2;
		final Integer MAX_DESCRIPTION_LENGTH=20;
		final Integer MIN_DESCRIPTION_LENGTH=2;
		
		Task task=(Task)o;
		String name=task.getName().trim(); 
		String description=task.getDescription().trim();
		
		//controlla che i campi non siano vuoti o che superino min e max con
		if (name.trim().isEmpty()) errors.rejectValue("name","required");
		else if (name.length()< MIN_NAME_LENGTH || name.length()>MAX_NAME_LENGTH) errors.rejectValue("name","size");

		if (description.trim().isEmpty()) errors.rejectValue("description","required");
		else if (description.length()< MIN_DESCRIPTION_LENGTH || description.length()>MAX_DESCRIPTION_LENGTH) errors.rejectValue("description","size");
	}
	

}
