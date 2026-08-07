package school.hei.demo.repository.mapper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
    if (jReservation == null) {
      return null;
    }

    List<UUID> seatIds =
        (jReservation.getSeats() == null)
            ? Collections.emptyList()
            : jReservation.getSeats().stream().map(JSeat::getId).collect(Collectors.toList());

    return new Reservation(
        jReservation.getId(),
        jReservation.getCreatedAt(),
        jReservation.getStatus(),
        jReservation.getProjection() != null ? jReservation.getProjection().getId() : null,
        jReservation.getClient() != null ? jReservation.getClient().getId() : null,
        jReservation.getEmployee() != null ? jReservation.getEmployee().getId() : null,
        seatIds);
  }

  public JReservation toEntity(
      Reservation reservation,
      JProjection projection,
      JUser client,
      JUser employee,
      List<JSeat> seats) {
    if (reservation == null) {
      return null;
    }

    return new JReservation(
        reservation.getId(),
        reservation.getCreatedAt(),
        reservation.getStatus(),
        projection,
        client,
        employee,
        seats);
  }
}
