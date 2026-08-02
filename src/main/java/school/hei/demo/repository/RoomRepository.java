package school.hei.demo.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.demo.repository.model.JRoom;

@Repository
public interface RoomRepository extends JpaRepository<JRoom, UUID> {}
