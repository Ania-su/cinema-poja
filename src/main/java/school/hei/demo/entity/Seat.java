package school.hei.demo.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Seat {
  private UUID id;
  private String number;
  private UUID roomId;
}
