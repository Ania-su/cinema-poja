package school.hei.demo.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.demo.repository.model.JUser;

@Repository
public interface UserRepository extends JpaRepository<JUser, UUID> {
  Optional<JUser> findByEmail(String email);
}
