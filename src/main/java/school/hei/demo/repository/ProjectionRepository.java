package school.hei.demo.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.demo.repository.model.JProjection;

@Repository
public interface ProjectionRepository extends JpaRepository<JProjection, UUID> {}
