package school.hei.demo.entity;

import java.util.List;
import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {
  private UUID id;
  private String number;
  private int capacity;

  @ToString.Exclude @EqualsAndHashCode.Exclude private List<Seat> seats;
}
