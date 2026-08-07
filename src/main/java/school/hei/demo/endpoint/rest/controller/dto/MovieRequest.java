package school.hei.demo.endpoint.rest.controller.dto;

import java.time.Duration;
import java.util.List;
import lombok.Data;
import school.hei.demo.entity.enums.Genre;

@Data
public class MovieRequest {
  private String title;
  private List<Genre> genres;
  private String description;
  private Duration duration;
}
