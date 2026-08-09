package school.hei.demo.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.LocalDate;
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
import school.hei.demo.endpoint.rest.controller.dto.RegisterRequest;
import school.hei.demo.entity.enums.UserRole;

class AuthControllerIT extends FacadeIT {

  private static final String REGISTER_URL = "/register";
  private static final String LOGIN_URL = "/login";

  @Autowired private TestRestTemplate testRestTemplate;

  @BeforeEach
  void setUp() {
    testRestTemplate.getRestTemplate().setRequestFactory(new JdkClientHttpRequestFactory());
  }

  @Test
  void register_validRequest_returns201AndCookie() {
    var request = registerRequest(uniqueEmail());

    var response =
        testRestTemplate.exchange(
            REGISTER_URL, POST, new HttpEntity<>(request, jsonHeaders()), String.class);

    assertEquals(CREATED, response.getStatusCode());
    assertEquals("Register successfully", response.getBody());
    assertTrue(response.getHeaders().containsKey("Set-Cookie"));
    assertTrue(response.getHeaders().getFirst("Set-Cookie").startsWith("jwt="));
  }

  @Test
  void register_duplicateEmail_returns400() {
    var email = uniqueEmail();
    var request = registerRequest(email);

    testRestTemplate.exchange(
        REGISTER_URL, POST, new HttpEntity<>(request, jsonHeaders()), String.class);

    var response =
        testRestTemplate.exchange(
            REGISTER_URL,
            POST,
            new HttpEntity<>(registerRequest(email), jsonHeaders()),
            String.class);

    assertEquals(BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void register_missingPassword_returns401() {
    var request = registerRequest(uniqueEmail());
    request.setPassword(null);

    var response =
        testRestTemplate.exchange(
            REGISTER_URL, POST, new HttpEntity<>(request, jsonHeaders()), String.class);

    assertEquals(UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void login_validCredentials_returns200AndCookie() {
    var email = uniqueEmail();
    var password = "S3cur3P@ss!";
    registerUser(email, password);

    var loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);

    var response =
        testRestTemplate.exchange(
            LOGIN_URL, POST, new HttpEntity<>(loginRequest, jsonHeaders()), String.class);

    assertEquals(OK, response.getStatusCode());
    assertEquals("Login successfully", response.getBody());
    assertTrue(response.getHeaders().containsKey("Set-Cookie"));
  }

  @Test
  void login_wrongPassword_returns401() {
    var email = uniqueEmail();
    registerUser(email, "correctPassword1!");

    var loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword("wrongPassword1!");

    var response =
        testRestTemplate.exchange(
            LOGIN_URL, POST, new HttpEntity<>(loginRequest, jsonHeaders()), String.class);

    assertEquals(UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void login_nonExistentEmail_returns401() {
    var loginRequest = new LoginRequest();
    loginRequest.setEmail("noone-" + UUID.randomUUID() + "@example.com");
    loginRequest.setPassword("password123");

    var response =
        testRestTemplate.exchange(
            LOGIN_URL, POST, new HttpEntity<>(loginRequest, jsonHeaders()), String.class);

    assertEquals(UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void register_requestedRoleManager_currentlyReturns201_SECURITY_BUG() {
    var request = registerRequest(uniqueEmail());
    request.setRole(UserRole.MANAGER);

    var response =
        testRestTemplate.exchange(
            REGISTER_URL, POST, new HttpEntity<>(request, jsonHeaders()), String.class);

    assertEquals(CREATED, response.getStatusCode());
  }

  private void registerUser(String email, String password) {
    var request = registerRequest(email);
    request.setPassword(password);

    testRestTemplate.exchange(
        REGISTER_URL, POST, new HttpEntity<>(request, jsonHeaders()), String.class);
  }

  private RegisterRequest registerRequest(String email) {
    var request = new RegisterRequest();
    request.setFirstName("Ania");
    request.setLastName("Razafy");
    request.setBirthdate(LocalDate.of(2000, 5, 14));
    request.setEmail(email);
    request.setPassword("S3cur3P@ss!");
    request.setPhone("+261340000000");
    request.setRole(UserRole.CLIENT);
    return request;
  }

  private String uniqueEmail() {
    return "user-" + UUID.randomUUID() + "@example.com";
  }

  private HttpHeaders jsonHeaders() {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    return headers;
  }
}
