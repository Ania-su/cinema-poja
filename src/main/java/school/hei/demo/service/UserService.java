package school.hei.demo.service;

import com.amazonaws.services.lambda.runtime.events.S3ObjectLambdaEvent.UserRequest;
import java.util.List;
import java.util.UUID;
import school.hei.demo.entity.User;

public class UserService {

  public User getCurrentUser() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCurrentUser'");
  }

  public List<User> getAllUsers() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
  }

  public User createUser(UserRequest newUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createUser'");
  }

  public User getUserById(UUID id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
  }

  public User updateUser(UUID id, UserRequest newUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
  }

  public void deleteUser(UUID id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
  }
}
