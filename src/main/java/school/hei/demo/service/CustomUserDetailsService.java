package school.hei.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import school.hei.demo.entity.User;
import school.hei.demo.repository.UserRepository;
import school.hei.demo.repository.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public User loadUserByUsername(String email) throws UsernameNotFoundException {
    return userMapper.toDomain(
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email)));
  }
}
