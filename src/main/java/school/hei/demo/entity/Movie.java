package school.hei.demo.entity;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.*;
import school.hei.demo.entity.enums.Genre;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

  private UUID id;
  private String title;
  private List<Genre> genres;
  private String description;
  private Duration duration;
}
