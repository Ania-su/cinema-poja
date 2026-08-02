package school.hei.demo.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import school.hei.demo.repository.model.JMovie;

public interface MovieRepository
    extends JpaRepository<JMovie, UUID>, JpaSpecificationExecutor<JMovie> {}
