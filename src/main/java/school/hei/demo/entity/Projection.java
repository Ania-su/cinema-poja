package school.hei.demo.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Projection {
  private UUID id;
  private Instant datetime;
  private BigDecimal seatPrice;
  private UUID movieId;
  private UUID roomId;
}
