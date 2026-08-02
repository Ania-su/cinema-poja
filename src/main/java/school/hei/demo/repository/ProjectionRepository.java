package school.hei.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.demo.repository.model.JProjection;

import java.util.UUID;

@Repository
public interface ProjectionRepository extends JpaRepository<JProjection, UUID> {}
