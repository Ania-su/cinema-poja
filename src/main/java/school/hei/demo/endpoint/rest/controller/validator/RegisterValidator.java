package school.hei.demo.endpoint.rest.controller.validator;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.demo.endpoint.rest.controller.dto.RegisterRequest;
import school.hei.demo.exception.EmailAlreadyTakenException;
import school.hei.demo.exception.InvalidCredentialsException;
import school.hei.demo.repository.UserRepository;

@Component
@AllArgsConstructor
public class RegisterValidator {
  private final UserRepository userRepository;

  public void validate(RegisterRequest request) {
    StringBuilder errors = new StringBuilder();

    if (request.getFirstName() == null || request.getFirstName().isBlank()) {
      errors.append("First name cannot be empty. ");
    }
    if (request.getLastName() == null || request.getLastName().isBlank()) {
      errors.append("Last name cannot be empty. ");
    }
    if (request.getBirthdate() == null) {
      errors.append("Birthdate cannot be empty. ");
    } else if (request.getBirthdate().isAfter(LocalDate.now())) {
      errors.append("Birthdate must be in the past. ");
    }
    if (request.getEmail() == null || request.getEmail().isBlank()) {
      errors.append("Email cannot be empty. ");
    } else if (!request.getEmail().matches(ValidationPatterns.EMAIL_REGEX)) {
      errors.append("Email must be valid. ");
    }
    if (request.getPassword() == null || request.getPassword().length() < 8) {
      errors.append("Password must be at least 8 characters long. ");
    }
    if (request.getRole() == null) {
      errors.append("Role cannot be empty. ");
    }

    if (!errors.isEmpty()) {
      throw new InvalidCredentialsException(errors.toString().trim());
    } else if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new EmailAlreadyTakenException("Email already taken");
    }
  }
}
