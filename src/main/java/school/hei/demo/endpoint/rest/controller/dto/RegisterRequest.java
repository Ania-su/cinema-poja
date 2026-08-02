package school.hei.demo.endpoint.rest.controller.dto;

import java.time.LocalDate;
import lombok.Data;
import school.hei.demo.entity.enums.UserRole;

@Data
public class RegisterRequest {
  private String firstName;
  private String lastName;
  private LocalDate birthdate;
  private String email;
  private String password;
  private String phone;
  private UserRole role;
}
