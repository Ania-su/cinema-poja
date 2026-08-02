package school.hei.demo.repository.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String number;

  @Column(nullable = false)
  private int capacity;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JSeat> seats;

  @OneToMany(mappedBy = "room")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JProjection> projections;
}
