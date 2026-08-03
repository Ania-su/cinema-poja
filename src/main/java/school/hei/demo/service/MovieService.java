package school.hei.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.demo.endpoint.rest.controller.dto.MovieRequest;
import school.hei.demo.entity.Movie;
import school.hei.demo.exception.NotFoundException;
import school.hei.demo.repository.MovieRepository;
import school.hei.demo.repository.mapper.MovieMapper;
import school.hei.demo.repository.model.JMovie;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public List<Movie> getMovies() {
        return movieMapper.toDomain(movieRepository.findAll());
    }

    public Movie getMovieById(UUID id) {
        JMovie jMovie =
                movieRepository.findById(id).orElseThrow(() -> new NotFoundException("Movie not found"));
        return movieMapper.toDomain(jMovie);
    }

    public Movie createMovie(MovieRequest request) {
        Movie toSave = new Movie();
        toSave.setTitle(request.getTitle());
        toSave.setGenres(request.getGenres());
        toSave.setDescription(request.getDescription());
        toSave.setDuration(request.getDuration());
        return movieMapper.toDomain(movieRepository.save(movieMapper.toJpa(toSave)));
    }

    public Movie updateMovie(UUID id, MovieRequest request) {
        JMovie existing =
                movieRepository.findById(id).orElseThrow(() -> new NotFoundException("Movie not found"));
        existing.setTitle(request.getTitle());
        existing.setGenres(request.getGenres());
        existing.setDescription(request.getDescription());
        existing.setDuration(request.getDuration());
        return movieMapper.toDomain(movieRepository.save(existing));
    }

//    public void deleteMovie(UUID id) {
//        if (!movieRepository.existsById(id)) {
//            throw new NotFoundException("Movie not found");
//        }
//        movieRepository.deleteById(id);
//    }
}
