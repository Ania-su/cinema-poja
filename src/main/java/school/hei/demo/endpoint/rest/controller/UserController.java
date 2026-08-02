package school.hei.demo.endpoint.rest.controller;

import com.amazonaws.services.lambda.runtime.events.S3ObjectLambdaEvent.UserRequest;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.demo.entity.User;
import school.hei.demo.service.UserService;

@AllArgsConstructor
@RestController
public class UserController {
  private final UserService userService;

  @GetMapping("/users/me")
  public User getCurrentUser() {
    return userService.getCurrentUser();
  }

  @GetMapping("/users")
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @PostMapping("path")
  public User creatUser(@RequestBody UserRequest newUser) {
    return userService.createUser(newUser);
  }

  @GetMapping("/users/{id}")
  public User getUserById(@PathVariable UUID id) {
    return userService.getUserById(id);
  }

  @PatchMapping("/users/{id}")
  public User updateUser(@PathVariable UUID id, @RequestBody UserRequest newUser) {
    return userService.updateUser(id, newUser);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
  }
}
