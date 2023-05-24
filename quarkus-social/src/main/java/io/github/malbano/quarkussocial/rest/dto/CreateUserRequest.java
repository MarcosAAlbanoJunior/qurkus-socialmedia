package io.github.malbano.quarkussocial.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateUserRequest {
	
	@NotBlank(message = "Name is Required")
	private String name;
	
	@NotNull(message = "Age is Required")
	private Integer age;
	
}
