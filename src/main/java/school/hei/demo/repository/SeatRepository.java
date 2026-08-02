package school.hei.demo.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.demo.repository.model.JSeat;

@Repository
public interface SeatRepository extends JpaRepository<JSeat, UUID> {
  List<JSeat> findByRoomId(UUID roomId);
}
