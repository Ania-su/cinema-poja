package school.hei.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.demo.endpoint.rest.controller.dto.ProjectionRequest;
import school.hei.demo.entity.Projection;
import school.hei.demo.repository.ProjectionRepository;
import school.hei.demo.repository.mapper.ProjectionMapper;
import school.hei.demo.repository.model.JMovie;
import school.hei.demo.repository.model.JProjection;
import school.hei.demo.repository.model.JRoom;

@AllArgsConstructor
@Service
public class ProjectionService {
  private final ProjectionRepository projectionRepository;
  private final ProjectionMapper projectionMapper;
  private final MovieService movieService;
  private final RoomService roomService;
  private final EntityManager entityManager;

  public List<Projection> getAllProjections() {
    return projectionRepository.findAll().stream()
        .map(projectionMapper::toDomain)
        .collect(Collectors.toList());
  }

  public Projection updateProjection(UUID id, ProjectionRequest projectionRequest) {
    JProjection jProjection =
        projectionRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Projection not found with id: " + id));
    movieService.findById(projectionRequest.getMovieId());
    roomService.findById(projectionRequest.getRoomId());

    jProjection.setDatetime(projectionRequest.getDatetime());
    jProjection.setSeatPrice(projectionRequest.getSeatPrice());
    jProjection.setMovie(entityManager.getReference(JMovie.class, projectionRequest.getMovieId()));
    jProjection.setRoom(entityManager.getReference(JRoom.class, projectionRequest.getRoomId()));
    jProjection = projectionRepository.save(jProjection);
    return projectionMapper.toDomain(jProjection);
  }
}
