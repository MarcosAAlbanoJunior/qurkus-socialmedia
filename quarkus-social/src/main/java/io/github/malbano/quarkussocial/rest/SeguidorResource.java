package io.github.malbano.quarkussocial.rest;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.malbano.quarkussocial.domain.model.Seguidor;
import io.github.malbano.quarkussocial.domain.repository.SeguidorRepository;
import io.github.malbano.quarkussocial.domain.repository.UserRepository;
import io.github.malbano.quarkussocial.rest.dto.SeguidorPorUsuarioResponse;
import io.github.malbano.quarkussocial.rest.dto.SeguidorRequest;
import io.github.malbano.quarkussocial.rest.dto.SeguidorResponse;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeguidorResource {

	@Inject
	private SeguidorRepository seguidorRepository;

	@Inject
	private UserRepository userRepository;

	@PUT
	@Transactional
	public Response SeguirUsuario(@PathParam("userId") Long userId, SeguidorRequest request) {

		var user = userRepository.findById(userId);

		if (userId.equals(request.getSeguidorId())) {
			return Response.status(Response.Status.CONFLICT).entity("Não pode serguir voce mesmo").build();
		}

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		var seguidor = userRepository.findById(request.getSeguidorId());

		boolean follows = seguidorRepository.follows(seguidor, user);

		if (!follows) {
			var entity = new Seguidor();
			entity.setUser(user);
			entity.setSeguidor(seguidor);

			seguidorRepository.persist(entity);
		}

		return Response.status(Response.Status.NO_CONTENT).build();

	}

	@GET
	public Response listFollowers(@PathParam("userId") Long userId) {

		var user = userRepository.findById(userId);
		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		var list = seguidorRepository.findByUser(userId);
		SeguidorPorUsuarioResponse responseObject = new SeguidorPorUsuarioResponse();
		responseObject.setQuantidadeSeguidores(list.size());

		var followerList = list.stream().map(SeguidorResponse::new).collect(Collectors.toList());

		responseObject.setContent(followerList);
		return Response.ok(responseObject).build();
	}
	
	@DELETE
	@Transactional
	public Response unfollow(@PathParam("userId") Long userId,@QueryParam("followerId") Long followerId) {
		var user = userRepository.findById(userId);
		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		seguidorRepository.deleteByFollowerAndUser(followerId, userId);
		
		return Response.status(Response.Status.NO_CONTENT).build();
	}

}
