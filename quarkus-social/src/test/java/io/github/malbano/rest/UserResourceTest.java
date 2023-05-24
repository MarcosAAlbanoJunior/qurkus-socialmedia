package io.github.malbano.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.github.malbano.quarkussocial.rest.dto.CreateUserRequest;
import io.github.malbano.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

	@TestHTTPResource("/users")
	URL apiUrl;

	@Test
	@DisplayName("Deveria criar um usuario com sucesso")
	@Order(1)
	public void createUserTest() {
		var user = new CreateUserRequest();
		user.setName("fulano");
		user.setAge(26);

		var response = given()
						.contentType(ContentType.JSON)
						.body(user).when().post(apiUrl)
						.then().extract().response();

		assertEquals(201, response.statusCode());
		assertNotNull(response.jsonPath().getString("id"));
	}

	@Test
	@Order(2)
	@DisplayName("Deveria retorna erro json nao é valido")
	public void createUserValidationTest() {
		var user = new CreateUserRequest();
		user.setName(null);
		user.setAge(null);

		var response = given()
						.contentType(ContentType.JSON)
						.body(user).when().post(apiUrl)
						.then().extract().response();

		assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
		assertEquals("Validation Error", response.jsonPath().getString("message"));
	}

	@Test
	@Order(3)
	@DisplayName("Deveria retornar usuario com sucesso")
	public void getUserTest() {
		given().contentType(ContentType.JSON)
			.when().get(apiUrl)
			.then()
			.statusCode(200)
			.body("size()", Matchers.is(1));
	}
}
