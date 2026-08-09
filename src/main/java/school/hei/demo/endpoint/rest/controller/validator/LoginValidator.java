package school.hei.demo.endpoint.rest.controller.validator;

import org.springframework.stereotype.Component;
import school.hei.demo.endpoint.rest.controller.dto.LoginRequest;
import school.hei.demo.exception.InvalidCredentialsException;

@Component
public class LoginValidator {
  public void validate(LoginRequest loginRequest) {
    StringBuilder errors = new StringBuilder();
    if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank()) {
      errors.append("Email cannot be empty. ");
    } else if (!loginRequest.getEmail().matches(ValidationPatterns.EMAIL_REGEX)) {
      errors.append("Email must be valid. ");
    }
    if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
      errors.append("Password cannot be empty. ");
    }

    if (!errors.isEmpty()) {
      throw new InvalidCredentialsException(errors.toString().trim());
    }
  }
}
