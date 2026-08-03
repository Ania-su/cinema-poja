package school.hei.demo.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.demo.entity.Projection;
import school.hei.demo.repository.model.JMovie;
import school.hei.demo.repository.model.JProjection;
import school.hei.demo.repository.model.JRoom;

@Component
public class ProjectionMapper {

  public Projection toDomain(JProjection jProjection) {
    return new Projection(
        jProjection.getId(),
        jProjection.getDatetime(),
        jProjection.getSeatPrice(),
        jProjection.getMovie().getId(),
        jProjection.getRoom().getId());
  }

  public JProjection toEntity(Projection projection, JMovie movie, JRoom room) {
    return new JProjection(
        projection.getId(), projection.getDatetime(), projection.getSeatPrice(), movie, room, null);
  }
}
