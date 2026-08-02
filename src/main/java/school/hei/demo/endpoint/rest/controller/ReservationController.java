package school.hei.demo.endpoint.rest.controller;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.demo.entity.Reservation;
import school.hei.demo.service.ReservationService;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {
  private final ReservationService reservationService;

  @GetMapping
  public Reservation getReservation() {
    return reservationService.getReservation();
  }

  @GetMapping("/{id}")
  public Reservation getReservationById(@PathVariable UUID id) {
    return reservationService.getReservationById(id);
  }

  @PutMapping("/{id}")
  public Reservation updateReservation(@PathVariable UUID id) {
    return reservationService.updateReservation(id);
  }
}
