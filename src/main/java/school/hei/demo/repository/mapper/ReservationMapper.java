package school.hei.demo.repository.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import school.hei.demo.entity.Reservation;
import school.hei.demo.repository.model.JProjection;
import school.hei.demo.repository.model.JReservation;
import school.hei.demo.repository.model.JSeat;
import school.hei.demo.repository.model.JUser;

@Component
public class ReservationMapper {

  public Reservation toDomain(JReservation jReservation) {
    return new Reservation(
        jReservation.getId(),
        jReservation.getCreatedAt(),
        jReservation.getStatus(),
        jReservation.getProjection().getId(),
        jReservation.getUser().getId(),
        jReservation.getSeats().stream().map(JSeat::getId).collect(Collectors.toList()));
  }

  public JReservation toEntity(
      Reservation reservation, JProjection projection, JUser user, List<JSeat> seats) {
    return new JReservation(
        reservation.getId(),
        reservation.getCreatedAt(),
        reservation.getStatus(),
        projection,
        user,
        seats);
  }
}
