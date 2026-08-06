package school.hei.demo.endpoint.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.demo.endpoint.rest.controller.dto.ProjectionRequest;
import school.hei.demo.entity.Projection;
import school.hei.demo.service.ProjectionService;

@AllArgsConstructor
@RestController
@RequestMapping("/projections")
public class ProjectionController {
  private final ProjectionService projectionService;

  @GetMapping
  public List<Projection> getAllProjections() {
    return projectionService.getAllProjections();
  }

  @PutMapping("/{id}")
  public Projection updateProjection(@PathVariable UUID id, ProjectionRequest projectionRequest) {
    return projectionService.updateProjection(id, projectionRequest);
  }
}
