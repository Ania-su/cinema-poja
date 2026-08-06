package school.hei.demo.endpoint.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.demo.endpoint.rest.controller.dto.MovieRequest;
import school.hei.demo.entity.Movie;
import school.hei.demo.service.MovieService;

@RequiredArgsConstructor
@RestController
public class MovieController {

  private final MovieService service;

  @GetMapping("/movies")
  public ResponseEntity<List<Movie>> getMovies() {
    return ResponseEntity.ok(service.getMovies());
  }

  @PostMapping("/movies")
  public ResponseEntity<Movie> createMovie(@RequestBody MovieRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.createMovie(request));
  }

  @GetMapping("/movies/{id}")
  public ResponseEntity<Movie> getMovieById(@PathVariable UUID id) {
    return ResponseEntity.ok(service.getMovieById(id));
  }

  @PutMapping("/movies/{id}")
  public ResponseEntity<Movie> updateMovie(@PathVariable UUID id, @RequestBody MovieRequest request) {
    return ResponseEntity.ok(service.updateMovie(id, request));
  }
}
