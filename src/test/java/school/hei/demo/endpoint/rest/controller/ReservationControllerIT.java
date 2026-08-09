package school.hei.demo.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.GET;
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
import school.hei.demo.endpoint.rest.controller.dto.RegisterRequest;
import school.hei.demo.endpoint.rest.controller.dto.ReservationRequest;
import school.hei.demo.entity.Reservation;
import school.hei.demo.entity.enums.Genre;
import school.hei.demo.entity.enums.ReservationStatus;
import school.hei.demo.entity.enums.UserRole;
import school.hei.demo.repository.MovieRepository;
import school.hei.demo.repository.ProjectionRepository;
import school.hei.demo.repository.RoomRepository;
import school.hei.demo.repository.SeatRepository;
import school.hei.demo.repository.UserRepository;
import school.hei.demo.repository.model.JMovie;
import school.hei.demo.repository.model.JProjection;
import school.hei.demo.repository.model.JRoom;
import school.hei.demo.repository.model.JSeat;

class ReservationControllerIT extends FacadeIT {

  private static final String RESERVATIONS_URL = "/reservations";
  private static final String REGISTER_URL = "/register";
  private static final String LOGIN_URL = "/login";

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private RoomRepository roomRepository;
  @Autowired private MovieRepository movieRepository;
  @Autowired private ProjectionRepository projectionRepository;
  @Autowired private SeatRepository seatRepository;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    testRestTemplate.getRestTemplate().setRequestFactory(new JdkClientHttpRequestFactory());
  }

  @Test
  void getAllReservations_asClient_returns403() {
    var headers = userHttpHeaders(uniqueEmail(), UserRole.CLIENT);

    var response =
        testRestTemplate.exchange(RESERVATIONS_URL, GET, new HttpEntity<>(headers), Map[].class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void getAllReservations_asManager_returnsOk() {
    var email = uniqueEmail();

    var clientHeaders = userHttpHeaders(email, UserRole.CLIENT);

    createReservationData(email, clientHeaders);

    var managerHeaders = userHttpHeaders(uniqueEmail(), UserRole.MANAGER);

    var response =
        testRestTemplate.exchange(
            RESERVATIONS_URL, GET, new HttpEntity<>(managerHeaders), Reservation[].class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().length == 0);
  }

  @Test
  void getAllReservations_asEmployee_returnsOk() {
    var headers = userHttpHeaders(uniqueEmail(), UserRole.EMPLOYEE);

    var response =
        testRestTemplate.exchange(
            RESERVATIONS_URL, GET, new HttpEntity<>(headers), Reservation[].class);

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  void getAllReservations_withoutToken_returns403() {
    var response =
        testRestTemplate.exchange(
            RESERVATIONS_URL, GET, new HttpEntity<>(new HttpHeaders()), String.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void getReservationById_asOwner_returnsOk() {
    var email = uniqueEmail();

    var clientHeaders = userHttpHeaders(email, UserRole.CLIENT);

    var reservationData = createReservationData(email, clientHeaders);

    var response =
        testRestTemplate.exchange(
            RESERVATIONS_URL + "/" + reservationData.reservation().getId(),
            GET,
            new HttpEntity<>(clientHeaders),
            Reservation.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());

    assertEquals(reservationData.reservation().getId(), response.getBody().getId());
  }

  @Test
  void getReservationById_withUnknownId_returns404() {
    var headers = userHttpHeaders(uniqueEmail(), UserRole.EMPLOYEE);

    var response =
        testRestTemplate.exchange(
            RESERVATIONS_URL + "/" + UUID.randomUUID(),
            GET,
            new HttpEntity<>(headers),
            String.class);

    assertEquals(NOT_FOUND, response.getStatusCode());
  }

  @Test
  void getReservationById_asAnotherClient_currentlyReturnsOk() {
    var ownerEmail = uniqueEmail();

    var ownerHeaders = userHttpHeaders(ownerEmail, UserRole.CLIENT);

    var reservationData = createReservationData(ownerEmail, ownerHeaders);

    var otherClientHeaders = userHttpHeaders(uniqueEmail(), UserRole.CLIENT);

    var response =
        testRestTemplate.exchange(
            RESERVATIONS_URL + "/" + reservationData.reservation().getId(),
            GET,
            new HttpEntity<>(otherClientHeaders),
            Map.class);

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  void updateReservation_asEmployee_returnsOk() {
    var clientEmail = uniqueEmail();

    var clientHeaders = userHttpHeaders(clientEmail, UserRole.CLIENT);

    var reservationData = createReservationData(clientEmail, clientHeaders);

    var employeeHeaders = userHttpHeaders(uniqueEmail(), UserRole.EMPLOYEE);

    var response =
        testRestTemplate.exchange(
            RESERVATIONS_URL + "/" + reservationData.reservation().getId(),
            PUT,
            new HttpEntity<>(ReservationStatus.SUCCESS, employeeHeaders),
            Map.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());

    assertEquals("SUCCESS", response.getBody().get("status"));
  }

  private ReservationData createReservationData(String clientEmail) {
    var clientHeaders = userHttpHeaders(clientEmail, UserRole.CLIENT);

    return createReservationData(clientEmail, clientHeaders);
  }

  private ReservationData createReservationData(String clientEmail, HttpHeaders clientHeaders) {

    var room = createRoomWithSeats(2);

    var movie = createMovie();

    var projection = createProjection(movie, room);

    var clientId = userRepository.findByEmail(clientEmail).orElseThrow().getId();

    var seats = seatRepository.findByRoomId(room.getId());

    var request = new ReservationRequest();

    request.setCreatedAt(Instant.now());
    request.setStatus(ReservationStatus.PENDING);
    request.setProjectionId(projection.getId());
    request.setClientId(clientId);
    request.setSeatIds(seats.stream().map(JSeat::getId).toList());

    var response =
        testRestTemplate.exchange(
            RESERVATIONS_URL, POST, new HttpEntity<>(request, clientHeaders), Reservation.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());

    return new ReservationData(request, response.getBody());
  }

  private JRoom createRoomWithSeats(int seatCount) {
    var room = new JRoom();

    room.setNumber("Room " + UUID.randomUUID().toString().substring(0, 4));

    room.setCapacity(80);

    room = roomRepository.save(room);

    for (int i = 0; i < seatCount; i++) {
      var seat = new JSeat();

      seat.setNumber("A" + i);
      seat.setRoom(room);

      seatRepository.save(seat);
    }

    return room;
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

  private HttpHeaders managerHttpHeaders(String email) {
    return userHttpHeaders(email, UserRole.MANAGER);
  }

  private HttpHeaders userHttpHeaders(String email, UserRole role) {

    var password = "S3cur3P@ss!";

    var registerRequest = new RegisterRequest();

    registerRequest.setFirstName("Reservation");
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

  private String uniqueEmail() {
    return "reservation-" + UUID.randomUUID() + "@example.com";
  }

  private record ReservationData(ReservationRequest request, Reservation reservation) {}
}
