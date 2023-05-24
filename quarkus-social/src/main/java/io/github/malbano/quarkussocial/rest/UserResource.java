package io.github.malbano.quarkussocial.rest;

import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.malbano.quarkussocial.domain.model.User;
import io.github.malbano.quarkussocial.domain.repository.UserRepository;
import io.github.malbano.quarkussocial.rest.dto.CreateUserRequest;
import io.github.malbano.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource{
	
	@Inject
	private UserRepository repository;
	
	@Inject
	private Validator validator;
	
	
	@POST
	@Transactional
	public Response createUser(CreateUserRequest userRequest) {
		
		Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
		if(!violations.isEmpty()) {
			
			
			
			return ResponseError
					.createFromValidation(violations)
					.withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
		}
		
		User user = new User();
		user.setAge(userRequest.getAge());
		user.setName(userRequest.getName());
		
		repository.persist(user);
		
		return Response
					.status(Response.Status.CREATED.getStatusCode())
					.entity(user).build();
	}
	
	@GET
	public Response listAllUsers() {
		PanacheQuery<User> query = repository.findAll();
		return Response.ok(query.list()).build();
	}
	
	@DELETE
	@Transactional
	@Path("{id}")
	public Response deleteUser( @PathParam("id") Long id) {
		User user = repository.findById(id);
		if(user != null) {
			repository.delete(user);
			return Response.noContent().build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
	}
	
	@PUT
	@Transactional
	@Path("{id}")
	public Response updateUser( @PathParam("id") Long id, CreateUserRequest request) {
		User user = repository.findById(id);
		if(user != null) {
			user.setAge(request.getAge());
			user.setName(request.getName());
			return Response.noContent().build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}