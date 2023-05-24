package io.github.malbano.quarkussocial.rest;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.github.malbano.quarkussocial.domain.model.Post;
import io.github.malbano.quarkussocial.domain.model.User;
import io.github.malbano.quarkussocial.domain.repository.PostRepository;
import io.github.malbano.quarkussocial.domain.repository.SeguidorRepository;
import io.github.malbano.quarkussocial.domain.repository.UserRepository;
import io.github.malbano.quarkussocial.rest.dto.CreatePostRequest;
import io.github.malbano.quarkussocial.rest.dto.PostResponse;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private PostRepository postRepository;

	
	@Inject
	private SeguidorRepository seguidorRepository;

	@POST
	@Transactional
	public Response savePost(@PathParam("userId")Long userId, CreatePostRequest postRequest) {
		User user = userRepository.findById(userId);
		if(user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		Post post = new Post();
		post.setPostText(postRequest.getText());
		post.setUser(user);
		
		postRepository.persist(post);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@GET
	public Response listPosts(@PathParam("userId")Long UserId, @HeaderParam("followerId") Long followerId) {
		User user = userRepository.findById(UserId);
		if(user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		if(followerId == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		User follower = userRepository.findById(followerId);
		
        if(follower == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
		
		boolean follows = seguidorRepository.follows(follower, user);
		if(!follows) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		var query = postRepository.find("user", Sort.by("dataHora", Direction.Descending),user);
		
		var list = query.list();
		
		var postResponseList = list.stream()
										.map(PostResponse::fromEntity)
										.collect(Collectors.toList());
		
		return Response.ok(postResponseList).build();
	}
}
