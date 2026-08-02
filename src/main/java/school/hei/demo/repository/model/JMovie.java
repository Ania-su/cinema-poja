package school.hei.demo.repository.model;

import jakarta.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.*;
import school.hei.demo.entity.enums.Genre;

@Entity
@Table(name = "movie")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JMovie {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @ElementCollection(targetClass = Genre.class)
  @CollectionTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "genre")
  private List<Genre> genres;

  @Column(columnDefinition = "TEXT")
  private String description;

  private Duration duration;

  @OneToMany(mappedBy = "movie")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<JProjection> projections;
}
