package io.github.malbano.rest;

import static io.restassured.RestAssured.given;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.github.malbano.quarkussocial.domain.model.Post;
import io.github.malbano.quarkussocial.domain.model.Seguidor;
import io.github.malbano.quarkussocial.domain.model.User;
import io.github.malbano.quarkussocial.domain.repository.PostRepository;
import io.github.malbano.quarkussocial.domain.repository.SeguidorRepository;
import io.github.malbano.quarkussocial.domain.repository.UserRepository;
import io.github.malbano.quarkussocial.rest.PostResource;
import io.github.malbano.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostResourceTest {

	@Inject
	UserRepository userRepository;
	
	@Inject
	SeguidorRepository seguidorRepository;
	
	@Inject
	PostRepository postRepository;
	
	Long userId;
	Long userNotFollowerId;
	Long userFollowerId;

	
	@BeforeEach
	@Transactional
	public void setUp() {
		//usuario padrao
		var user = new User();
		user.setAge(30);
		user.setName("ciclano");
		userRepository.persist(user);
		userId = user.getId();
		
		//usuario nao segue
		var userNotFollower = new User();
		userNotFollower.setAge(30);
		userNotFollower.setName("fulano");
		userRepository.persist(userNotFollower);
		userNotFollowerId = userNotFollower.getId();
		
		
		//usuario segue
		var userFollower = new User();
		userFollower.setAge(30);
		userFollower.setName("fulano");
		userRepository.persist(userFollower);
		userFollowerId = userFollower.getId();
		
		Seguidor follower = new Seguidor();
		follower.setUser(user);
		follower.setSeguidor(userFollower);
		seguidorRepository.persist(follower);
		
		Post post = new Post();
		post.setPostText("programar is cool");
		post.setUser(user);
		postRepository.persist(post);
		
	}

	@Test
	@DisplayName("Deveria criar um Post com sucesso")
	@Order(1)
	public void createPostTest() {
		
		CreatePostRequest postRequest = new CreatePostRequest();
		postRequest.setText("texto teste");
		
		given()
			.contentType(ContentType.JSON)
			.body(postRequest)
			.pathParam("userId", userId)
			.when()
			.post()
			.then()
			.statusCode(201);
	}
	
	@Test
	@DisplayName("Deveria retornar 404 quando tentar fazer um Post")
	@Order(1)
	public void errorPostTest() {
		
		CreatePostRequest postRequest = new CreatePostRequest();
		postRequest.setText("texto teste");
		
		var inexsitenteId = 999;
		
		given()
			.contentType(ContentType.JSON)
			.body(postRequest)
			.pathParam("userId", inexsitenteId)
			.when()
			.post()
			.then()
			.statusCode(404);
	}
	
	@Test
	@DisplayName("Deveria retornar um 404 quando o usuario nao existir")
	public void listPostUserNotFoundTest() {
		var inexistenteUserId = 999;
		
		given()
			.pathParam("userId", inexistenteUserId)
			.when()
			.get()
			.then()
			.statusCode(404);
	}
	
	@Test
	@DisplayName("Deveria retornar um 400 quando o header nao for informado")
	public void listPostHeaderNotSendTest() {
		
		given()
			.pathParam("userId", userId)
			.when()
			.get()
			.then()
			.statusCode(400);
		
	}
	
	@Test
	@DisplayName("Deveria retornar um 400 quando o seguidor nao existir")
	public void listPostFollowerNotFoundTest() {

        var inexistentFollowerId = 999;

        given()
            .pathParam("userId", userId)
            .header("followerId", inexistentFollowerId)
        .when()
            .get()
        .then()
            .statusCode(400);
	}
	
	@Test
	@DisplayName("Deveria retornar um 403 quando o usuario nao é um seguidor")
	public void listPostUserNotIsAFollowerTest() {
		
		given()
			.pathParam("userId", userId)
			.header("followerId", userNotFollowerId)
			.when()
			.get()
			.then()
			.statusCode(403);
		
	}
	
	@Test
	@DisplayName("Deveria retornar um post com sucesso")
	public void listPostTest() {
		
		given()
			.pathParam("userId", userId)
			.header("followerId", userFollowerId)
			.when()
			.get()
			.then()
			.statusCode(200)
			.body("size()", Matchers.is(1));
		
	}
}
