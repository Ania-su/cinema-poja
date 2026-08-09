package school.hei.demo.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
import school.hei.demo.endpoint.rest.controller.dto.ProjectionRequest;
import school.hei.demo.endpoint.rest.controller.dto.RegisterRequest;
import school.hei.demo.entity.enums.Genre;
import school.hei.demo.entity.enums.UserRole;
import school.hei.demo.repository.MovieRepository;
import school.hei.demo.repository.ProjectionRepository;
import school.hei.demo.repository.RoomRepository;
import school.hei.demo.repository.model.JMovie;
import school.hei.demo.repository.model.JProjection;
import school.hei.demo.repository.model.JRoom;

class ProjectionControllerIT extends FacadeIT {

  private static final String PROJECTIONS_URL = "/projections";
  private static final String REGISTER_URL = "/register";
  private static final String LOGIN_URL = "/login";

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private RoomRepository roomRepository;
  @Autowired private MovieRepository movieRepository;
  @Autowired private ProjectionRepository projectionRepository;

  @BeforeEach
  void setUp() {
    testRestTemplate.getRestTemplate().setRequestFactory(new JdkClientHttpRequestFactory());
  }

  @Test
  void getProjections_isPublic_returnsOk() {
    createProjection(createMovie(), createRoom());

    var response = testRestTemplate.getForEntity(PROJECTIONS_URL, List.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().isEmpty());
  }

  @Test
  void updateProjection_asManager_returnsUpdatedProjection() {
    var movie = createMovie();
    var room = createRoom();
    var projection = createProjection(movie, room);
    var headers = managerHttpHeaders("mgr-projection-update@example.com");

    var request = projectionRequest(movie.getId(), room.getId(), new BigDecimal("15.00"));

    var response =
        testRestTemplate.exchange(
            PROJECTIONS_URL + "/" + projection.getId(),
            PUT,
            new HttpEntity<>(request, headers),
            Map.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(
        0,
        new BigDecimal("15.00")
            .compareTo(new BigDecimal(response.getBody().get("seatPrice").toString())));
  }

  @Test
  void updateProjection_asClient_returns403() {
    var movie = createMovie();
    var room = createRoom();
    var projection = createProjection(movie, room);
    var headers = userHttpHeaders("client-projection-update@example.com", UserRole.CLIENT);

    var request = projectionRequest(movie.getId(), room.getId(), new BigDecimal("99.00"));

    var response =
        testRestTemplate.exchange(
            PROJECTIONS_URL + "/" + projection.getId(),
            PUT,
            new HttpEntity<>(request, headers),
            Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void updateProjection_asEmployee_returns403() {
    var movie = createMovie();
    var room = createRoom();
    var projection = createProjection(movie, room);
    var headers = userHttpHeaders("employee-projection-update@example.com", UserRole.EMPLOYEE);

    var request = projectionRequest(movie.getId(), room.getId(), new BigDecimal("99.00"));

    var response =
        testRestTemplate.exchange(
            PROJECTIONS_URL + "/" + projection.getId(),
            PUT,
            new HttpEntity<>(request, headers),
            Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void updateProjection_withUnknownProjectionId_returns404() {
    var movie = createMovie();
    var room = createRoom();
    var headers = managerHttpHeaders("mgr-projection-404@example.com");

    var request = projectionRequest(movie.getId(), room.getId(), new BigDecimal("15.00"));

    var response =
        testRestTemplate.exchange(
            PROJECTIONS_URL + "/" + UUID.randomUUID(),
            PUT,
            new HttpEntity<>(request, headers),
            Map.class);

    assertEquals(NOT_FOUND, response.getStatusCode());
  }

  @Test
  void updateProjection_withUnknownMovieId_returns404() {
    var room = createRoom();
    var projection = createProjection(createMovie(), room);
    var headers = managerHttpHeaders("mgr-projection-unknown-movie@example.com");

    var request = projectionRequest(UUID.randomUUID(), room.getId(), new BigDecimal("15.00"));

    var response =
        testRestTemplate.exchange(
            PROJECTIONS_URL + "/" + projection.getId(),
            PUT,
            new HttpEntity<>(request, headers),
            Map.class);

    assertEquals(NOT_FOUND, response.getStatusCode());
  }

  @Test
  void updateProjection_withUnknownRoomId_returns404() {
    var movie = createMovie();
    var projection = createProjection(movie, createRoom());
    var headers = managerHttpHeaders("mgr-projection-unknown-room@example.com");

    var request = projectionRequest(movie.getId(), UUID.randomUUID(), new BigDecimal("15.00"));

    var response =
        testRestTemplate.exchange(
            PROJECTIONS_URL + "/" + projection.getId(),
            PUT,
            new HttpEntity<>(request, headers),
            Map.class);

    assertEquals(NOT_FOUND, response.getStatusCode());
  }

  private JRoom createRoom() {
    var room = new JRoom();
    room.setNumber("Room " + UUID.randomUUID().toString().substring(0, 4));
    room.setCapacity(80);
    return roomRepository.save(room);
  }

  private JMovie createMovie() {
    var movie = new JMovie();
    movie.setTitle("Fixture Movie " + UUID.randomUUID());
    movie.setGenres(List.of(Genre.DRAMA));
    movie.setDescription("Fixture description");
    movie.setDuration(Duration.ofMinutes(120));
    return movieRepository.save(movie);
  }

  private JProjection createProjection(JMovie movie, JRoom room) {
    var projection = new JProjection();
    projection.setDatetime(Instant.now().plus(1, ChronoUnit.DAYS));
    projection.setSeatPrice(new BigDecimal("12.50"));
    projection.setMovie(movie);
    projection.setRoom(room);
    return projectionRepository.save(projection);
  }

  private ProjectionRequest projectionRequest(UUID movieId, UUID roomId, BigDecimal seatPrice) {
    var request = new ProjectionRequest();
    request.setDatetime(Instant.now().plus(2, ChronoUnit.DAYS));
    request.setSeatPrice(seatPrice);
    request.setMovieId(movieId);
    request.setRoomId(roomId);
    return request;
  }

  private HttpHeaders managerHttpHeaders(String email) {
    return userHttpHeaders(email, UserRole.MANAGER);
  }

  private HttpHeaders userHttpHeaders(String email, UserRole role) {
    var password = "S3cur3P@ss!";

    var registerRequest = new RegisterRequest();
    registerRequest.setFirstName("Projection");
    registerRequest.setLastName("Tester");
    registerRequest.setBirthdate(LocalDate.of(2000, 5, 14));
    registerRequest.setEmail(email);
    registerRequest.setPassword(password);
    registerRequest.setPhone("+261340000000");
    registerRequest.setRole(role);

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

    var setCookie = loginResponse.getHeaders().getFirst("Set-Cookie");
    assertNotNull(setCookie);

    var headers = jsonHeaders();
    headers.set("Cookie", "jwt=" + extractJwt(setCookie));
    return headers;
  }

  private String extractJwt(String setCookie) {
    for (var part : setCookie.split(";")) {
      var cookie = part.trim();
      if (cookie.startsWith("jwt=")) {
        return cookie.substring("jwt=".length());
      }
    }
    throw new AssertionError("JWT cookie not found in Set-Cookie header");
  }

  private HttpHeaders jsonHeaders() {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    return headers;
  }
}
