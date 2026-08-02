package school.hei.demo.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.demo.entity.Movie;
import school.hei.demo.repository.model.JMovie;

@Component
public class MovieMapper {

  public Movie toDomain(JMovie jMovie) {
    return new Movie(
        jMovie.getId(),
        jMovie.getTitle(),
        jMovie.getGenres(),
        jMovie.getDescription(),
        jMovie.getDuration());
  }

  public JMovie toEntity(Movie movie) {
    return new JMovie(
        movie.getId(),
        movie.getTitle(),
        movie.getGenres(),
        movie.getDescription(),
        movie.getDuration(),
        null);
  }
}
