package school.hei.demo.repository.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.*;
import school.hei.demo.entity.enums.UserRole;

@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JUser {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  private LocalDate birthdate;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @OneToMany(mappedBy = "user")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JReservation> reservations;
}
