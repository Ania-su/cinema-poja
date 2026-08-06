package school.hei.demo.endpoint.rest.controller.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import school.hei.demo.entity.enums.ReservationStatus;

@Data
public class ReservationRequest {
  private Instant createdAt;
  private ReservationStatus status;
  private UUID projectionId;
  private UUID clientId;
  private List<UUID> seatIds;
}
