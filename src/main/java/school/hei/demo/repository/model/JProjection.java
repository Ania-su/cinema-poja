package school.hei.demo.repository.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "projection")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JProjection {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private Instant datetime;

  @Column(nullable = false)
  private BigDecimal seatPrice;

  @ManyToOne
  @JoinColumn(name = "movie_id", nullable = false)
  private JMovie movie;

  @ManyToOne
  @JoinColumn(name = "room_id", nullable = false)
  private JRoom room;

  @OneToMany(mappedBy = "projection")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JReservation> reservations;
}
