package school.hei.demo.endpoint.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.demo.endpoint.rest.controller.dto.ReservationRequest;
import school.hei.demo.entity.Reservation;
import school.hei.demo.entity.enums.ReservationStatus;
import school.hei.demo.service.ReservationService;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {
  private final ReservationService reservationService;

  @GetMapping
  public List<Reservation> getReservation() {
    return reservationService.getReservation();
  }

  @GetMapping("/{id}")
  public Reservation getReservationById(@PathVariable UUID id) {
    return reservationService.getReservationById(id);
  }

  @PutMapping("/{id}")
  public Reservation updateReservation(@PathVariable UUID id, @RequestBody ReservationStatus status) {
    return reservationService.updateReservation(id, status);
  }

  @PostMapping
  public Reservation createReservation(@RequestBody ReservationRequest newReservation) {
    return reservationService.createReservation(newReservation);
  }
}
