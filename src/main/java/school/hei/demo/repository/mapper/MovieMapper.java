package school.hei.demo.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.demo.entity.Movie;
import school.hei.demo.repository.model.JMovie;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieMapper {

    public Movie toDomain(JMovie jMovie) {
        if (jMovie == null) return null;
        Movie movie = new Movie();
        movie.setId(jMovie.getId());
        movie.setTitle(jMovie.getTitle());
        movie.setGenres(jMovie.getGenres());
        movie.setDescription(jMovie.getDescription());
        movie.setDuration(jMovie.getDuration());
        return movie;
    }

    public List<Movie> toDomain(List<JMovie> jMovies) {
        if (jMovies == null) return null;
        List<Movie> movies = new ArrayList<>();
        for (JMovie jm : jMovies) movies.add(toDomain(jm));
        return movies;
    }

    public JMovie toJpa(Movie movie) {
        if (movie == null) return null;
        JMovie jMovie = new JMovie();
        jMovie.setId(movie.getId());
        jMovie.setTitle(movie.getTitle());
        jMovie.setGenres(movie.getGenres());
        jMovie.setDescription(movie.getDescription());
        jMovie.setDuration(movie.getDuration());
        return jMovie;
    }
}