package school.hei.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import school.hei.demo.repository.model.JMovie;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<JMovie, UUID>,
        JpaSpecificationExecutor<JMovie> {
}
