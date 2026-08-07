package school.hei.demo.repository.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import school.hei.demo.entity.enums.ReservationStatus;

@Entity
@Table(name = "reservation")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JReservation {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private Instant createdAt;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "status", nullable = false, columnDefinition = "reservation_status")
  private ReservationStatus status;

  @ManyToOne
  @JoinColumn(name = "projection_id", nullable = false)
  private JProjection projection;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private JUser client;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private JUser employee;

  @ManyToMany
  @JoinTable(
      name = "reservation_seat",
      joinColumns = @JoinColumn(name = "reservation_id"),
      inverseJoinColumns = @JoinColumn(name = "seat_id"))
  private List<JSeat> seats;
}
