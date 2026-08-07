package school.hei.demo.endpoint.rest.controller.validator;

import lombok.AllArgsConstructor;
import school.hei.demo.endpoint.rest.controller.dto.MovieRequest;
import school.hei.demo.exception.BadRequestException;

@AllArgsConstructor
public class MovieValidator {
  private final MovieRequest movieRequest;

  public void validate() {
    StringBuilder errors = new StringBuilder();

    if (movieRequest.getTitle() == null || movieRequest.getTitle().isEmpty()) {
      errors.append("Movie title cannot be empty. ");
    }
    if (movieRequest.getGenres() == null || movieRequest.getGenres().isEmpty()) {
      errors.append("Movie genres cannot be empty. ");
    }
    if (movieRequest.getDescription() == null || movieRequest.getDescription().isEmpty()) {
      errors.append("Movie description cannot be empty. ");
    }
    if (movieRequest.getDuration() == null
        || movieRequest.getDuration().isNegative()
        || movieRequest.getDuration().isZero()) {
      errors.append("Movie duration cannot be negative or zero. ");
    }

    if (!errors.isEmpty()) {
      throw new BadRequestException(errors.toString().trim());
    }
  }
}
