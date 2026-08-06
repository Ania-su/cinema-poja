package school.hei.demo.endpoint.rest.controller.dto;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import school.hei.demo.entity.enums.Genre;

@Data
public class MovieRequest {
  private UUID id;
  private String title;
  private List<Genre> genres;
  private String description;
  private Duration duration;
}
