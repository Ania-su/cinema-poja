package school.hei.demo.endpoint.rest.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class ProjectionRequest {
  private Instant datetime;
  private BigDecimal seatPrice;
  private UUID movieId;
  private UUID roomId;
}
