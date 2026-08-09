package school.hei.demo.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import school.hei.demo.conf.FacadeIT;
import school.hei.demo.endpoint.rest.controller.dto.LoginRequest;
import school.hei.demo.endpoint.rest.controller.dto.MovieRequest;
import school.hei.demo.endpoint.rest.controller.dto.RegisterRequest;
import school.hei.demo.entity.enums.Genre;
import school.hei.demo.entity.enums.UserRole;

class MovieControllerIT extends FacadeIT {

  private static final String MOVIES_URL = "/movies";
  private static final String REGISTER_URL = "/register";
  private static final String LOGIN_URL = "/login";

  @Autowired private TestRestTemplate testRestTemplate;

  @BeforeEach
  void setUp() {
    testRestTemplate.getRestTemplate().setRequestFactory(new JdkClientHttpRequestFactory());
  }

  @Test
  void getMovies_returnsMovies() {
    var response = testRestTemplate.getForEntity(MOVIES_URL, List.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void getMovieById_returnsMovie() {
    var headers = managerHttpHeaders();

    var createRequest = movieRequest("Inception", "A mind-bending thriller");

    var createResponse =
        testRestTemplate.exchange(
            MOVIES_URL, POST, new HttpEntity<>(createRequest, headers), Map.class);

    assertEquals(CREATED, createResponse.getStatusCode());
    assertNotNull(createResponse.getBody());

    var movieId = UUID.fromString(createResponse.getBody().get("id").toString());

    var response = testRestTemplate.getForEntity(MOVIES_URL + "/" + movieId, Map.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());

    assertEquals(movieId.toString(), response.getBody().get("id").toString());

    assertEquals("Inception", response.getBody().get("title"));

    assertEquals("A mind-bending thriller", response.getBody().get("description"));
  }

  @Test
  void updateMovie_returnsUpdatedMovie() {
    var headers = managerHttpHeaders();

    var createRequest = movieRequest("Original Title", "Original description");

    var createResponse =
        testRestTemplate.exchange(
            MOVIES_URL, POST, new HttpEntity<>(createRequest, headers), Map.class);

    assertEquals(CREATED, createResponse.getStatusCode());
    assertNotNull(createResponse.getBody());

    var movieId = UUID.fromString(createResponse.getBody().get("id").toString());

    var updateRequest = movieRequest("Updated Title", "Updated description");

    var updateResponse =
        testRestTemplate.exchange(
            MOVIES_URL + "/" + movieId, PUT, new HttpEntity<>(updateRequest, headers), Map.class);

    assertEquals(OK, updateResponse.getStatusCode());
    assertNotNull(updateResponse.getBody());

    assertEquals(movieId.toString(), updateResponse.getBody().get("id").toString());

    assertEquals("Updated Title", updateResponse.getBody().get("title"));

    assertEquals("Updated description", updateResponse.getBody().get("description"));
  }

  @Test
  void createMovie_returnsCreatedMovie() {
    var headers = managerHttpHeaders();

    var request = movieRequest("Inception", "A mind-bending thriller");

    var response =
        testRestTemplate.exchange(MOVIES_URL, POST, new HttpEntity<>(request, headers), Map.class);

    assertEquals(CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Inception", response.getBody().get("title"));
    assertEquals("A mind-bending thriller", response.getBody().get("description"));
    assertNotNull(response.getBody().get("id"));
  }

  private HttpHeaders managerHttpHeaders() {
    var email = "manager-" + UUID.randomUUID() + "@example.com";

    var password = "S3cur3P@ss!";

    var registerRequest = new RegisterRequest();

    registerRequest.setFirstName("Movie");
    registerRequest.setLastName("Manager");
    registerRequest.setBirthdate(LocalDate.of(2000, 5, 14));
    registerRequest.setEmail(email);
    registerRequest.setPassword(password);
    registerRequest.setPhone("+261340000000");
    registerRequest.setRole(UserRole.MANAGER);

    var registerResponse =
        testRestTemplate.exchange(
            REGISTER_URL, POST, new HttpEntity<>(registerRequest, jsonHeaders()), String.class);

    assertEquals(CREATED, registerResponse.getStatusCode());

    var loginRequest = new LoginRequest();

    loginRequest.setEmail(email);
    loginRequest.setPassword(password);

    var loginResponse =
        testRestTemplate.exchange(
            LOGIN_URL, POST, new HttpEntity<>(loginRequest, jsonHeaders()), String.class);

    assertEquals(OK, loginResponse.getStatusCode());

    assertNotNull(loginResponse.getHeaders().getFirst("Set-Cookie"));

    var setCookie = loginResponse.getHeaders().getFirst("Set-Cookie");

    var jwt = extractJwt(setCookie);

    var headers = jsonHeaders();

    headers.set("Cookie", "jwt=" + jwt);

    return headers;
  }

  private String extractJwt(String setCookie) {
    var cookieParts = setCookie.split(";");

    for (var part : cookieParts) {
      var cookie = part.trim();

      if (cookie.startsWith("jwt=")) {
        return cookie.substring("jwt=".length());
      }
    }

    throw new AssertionError("JWT cookie not found in Set-Cookie header");
  }

  private MovieRequest movieRequest(String title, String description) {

    var request = new MovieRequest();

    request.setTitle(title);
    request.setDescription(description);
    request.setGenres(List.of(Genre.SCI_FI, Genre.THRILLER));
    request.setDuration(Duration.ofMinutes(148));

    return request;
  }

  private HttpHeaders jsonHeaders() {
    var headers = new HttpHeaders();

    headers.setContentType(APPLICATION_JSON);

    return headers;
  }
}
