package school.hei.demo.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.hei.demo.entity.enums.ReservationStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reservation {
  private UUID id;
  private Instant createdAt;
  private ReservationStatus status;
  private UUID projectionId;
  private UUID userId;
  private List<UUID> seatIds;
}
