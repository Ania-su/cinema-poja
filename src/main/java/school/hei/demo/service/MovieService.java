package school.hei.demo.service;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.demo.entity.Movie;
import school.hei.demo.exception.NotFoundException;
import school.hei.demo.repository.MovieRepository;
import school.hei.demo.repository.mapper.MovieMapper;
import school.hei.demo.repository.model.JMovie;

@Service
@AllArgsConstructor
public class MovieService {
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public Movie findById(UUID movieId) {
    JMovie jMovie =
        movieRepository
            .findById(movieId)
            .orElseThrow(() -> new NotFoundException("Movie not found with id: " + movieId));
    return movieMapper.toDomain(jMovie);
  }
}
