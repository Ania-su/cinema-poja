package school.hei.demo.endpoint.rest.controller.validator;

import lombok.AllArgsConstructor;
import school.hei.demo.endpoint.rest.controller.dto.LoginRequest;
import school.hei.demo.exception.BadRequestException;

@AllArgsConstructor
public class LoginValidator {
  private final LoginRequest loginRequest;

  public void validate() {
    StringBuilder errors = new StringBuilder();

    if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
      errors.append("Email cannot be empty. ");
    } else if (!loginRequest.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
      errors.append("Email must be valid. ");
    }
    if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
      errors.append("Password cannot be empty. ");
    }

    if (!errors.isEmpty()) {
      throw new BadRequestException(errors.toString().trim());
    }
  }
}
