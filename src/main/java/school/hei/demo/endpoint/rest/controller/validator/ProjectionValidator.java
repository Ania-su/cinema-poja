package school.hei.demo.endpoint.rest.controller.validator;

import java.time.Instant;
import org.springframework.stereotype.Component;
import school.hei.demo.endpoint.rest.controller.dto.ProjectionRequest;
import school.hei.demo.exception.BadRequestException;

@Component
public class ProjectionValidator {

  public void validate(ProjectionRequest projectionRequest) {
    StringBuilder errors = new StringBuilder();

    if (projectionRequest.getDatetime() == null) {
      errors.append("Projection datetime cannot be empty. ");
    } else if (projectionRequest.getDatetime().isBefore(Instant.now())) {
      errors.append("Projection datetime must be in the future. ");
    }
    if (projectionRequest.getSeatPrice() == null || projectionRequest.getSeatPrice().signum() < 0) {
      errors.append("Seat price cannot be negative or empty. ");
    }
    if (projectionRequest.getMovieId() == null) {
      errors.append("Movie id cannot be empty. ");
    }
    if (projectionRequest.getRoomId() == null) {
      errors.append("Room id cannot be empty. ");
    }

    if (!errors.isEmpty()) {
      throw new BadRequestException(errors.toString().trim());
    }
  }
}
