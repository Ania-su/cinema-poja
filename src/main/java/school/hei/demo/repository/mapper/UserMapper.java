package school.hei.demo.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.demo.entity.User;
import school.hei.demo.repository.model.JUser;

@Component
public class UserMapper {

  public User toDomain(JUser jUser) {
    if (jUser == null) return null;
    User user = new User();
    user.setId(jUser.getId());
    user.setFirstName(jUser.getFirstName());
    user.setLastName(jUser.getLastName());
    user.setBirthdate(jUser.getBirthdate());
    user.setEmail(jUser.getEmail());
    user.setPassword(jUser.getPassword());
    user.setPhone(jUser.getPhone());
    user.setRole(jUser.getRole());
    return user;
  }

  public JUser toJpa(User user) {
    if (user == null) return null;
    JUser jUser = new JUser();
    jUser.setId(user.getId());
    jUser.setFirstName(user.getFirstName());
    jUser.setLastName(user.getLastName());
    jUser.setBirthdate(user.getBirthdate());
    jUser.setEmail(user.getEmail());
    jUser.setPassword(user.getPassword());
    jUser.setPhone(user.getPhone());
    jUser.setRole(user.getRole());
    return jUser;
  }
}
