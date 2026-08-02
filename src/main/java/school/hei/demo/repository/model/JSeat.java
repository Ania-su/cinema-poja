package school.hei.demo.repository.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "seat")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JSeat {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String number;

  @ManyToOne
  @JoinColumn(name = "room_id", nullable = false)
  private JRoom room;

  @ManyToMany(mappedBy = "seats")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JReservation> reservations;
}
