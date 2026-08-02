package school.hei.demo.endpoint.rest.controller;

import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.demo.endpoint.rest.controller.dto.LoginRequest;
import school.hei.demo.endpoint.rest.controller.dto.RegisterRequest;
import school.hei.demo.exception.EmailAlreadyTakenException;
import school.hei.demo.exception.InvalidCredentialsException;
import school.hei.demo.service.AuthService;

@RestController
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    try {
      String token = authService.register(request);
      return ResponseEntity.status(201)
          .header(HttpHeaders.SET_COOKIE, buildCookie(token).toString())
          .body("Register successfully");
    } catch (EmailAlreadyTakenException e) {
      return ResponseEntity.status(409).body(e.getMessage());
    } catch (InvalidCredentialsException e) {
      return ResponseEntity.status(400).body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    try {
      String token = authService.login(request.getEmail(), request.getPassword());
      return ResponseEntity.status(200)
          .header(HttpHeaders.SET_COOKIE, buildCookie(token).toString())
          .body("Login successfully");
    } catch (InvalidCredentialsException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  private ResponseCookie buildCookie(String token) {
    return ResponseCookie.from("jwt", token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(Duration.ofHours(1))
        .sameSite("Strict")
        .build();
  }
}
