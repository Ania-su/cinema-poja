package school.hei.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import school.hei.demo.endpoint.rest.controller.dto.RegisterRequest;
import school.hei.demo.endpoint.rest.controller.validator.LoginValidator;
import school.hei.demo.entity.User;
import school.hei.demo.entity.enums.UserRole;
import school.hei.demo.exception.EmailAlreadyTakenException;
import school.hei.demo.exception.InvalidCredentialsException;
import school.hei.demo.repository.UserRepository;
import school.hei.demo.repository.mapper.UserMapper;
import school.hei.demo.repository.model.JUser;

@Service
@AllArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final UserMapper userMapper;
  private final LoginValidator loginValidator;

  public String register(RegisterRequest request) {
    if (request.getEmail() == null
        || request.getEmail().isBlank()
        || request.getPassword() == null
        || request.getPassword().isBlank()) {
      throw new InvalidCredentialsException("Email and password are required");
    }
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new EmailAlreadyTakenException("Email already taken");
    }

    User toSave = new User();
    toSave.setFirstName(request.getFirstName());
    toSave.setLastName(request.getLastName());
    toSave.setBirthdate(request.getBirthdate());
    toSave.setEmail(request.getEmail());
    toSave.setPassword(passwordEncoder.encode(request.getPassword()));
    toSave.setPhone(request.getPhone());
    toSave.setRole(request.getRole() != null ? request.getRole() : UserRole.CLIENT);

    JUser saved = userRepository.save(userMapper.toJpa(toSave));
    return jwtService.generateToken(userMapper.toDomain(saved));
  }

  public String login(String email, String password) {
    loginValidator.validate();

    JUser jUser =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

    User user = userMapper.toDomain(jUser);

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new InvalidCredentialsException("Invalid credentials");
    }

    return jwtService.generateToken(user);
  }
}
