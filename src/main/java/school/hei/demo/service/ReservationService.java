package school.hei.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.demo.entity.Reservation;
import school.hei.demo.entity.enums.ReservationStatus;
import school.hei.demo.exception.NotFoundException;
import school.hei.demo.repository.ReservationRepository;
import school.hei.demo.repository.mapper.ReservationMapper;
import school.hei.demo.repository.model.JReservation;

@AllArgsConstructor
@Service
public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;

  public List<Reservation> getReservation() {
    return reservationRepository.findAll().stream()
        .map(reservationMapper::toDomain)
        .collect(Collectors.toList());
  }

  public Reservation getReservationById(UUID id) {
    return reservationRepository
        .findById(id)
        .map(reservationMapper::toDomain)
        .orElseThrow(() -> new NotFoundException("Reservation not found with id: " + id));
  }

  public Reservation updateReservation(UUID id, ReservationStatus status) {
    JReservation jReservation =
        reservationRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Reservation not found with id: " + id));
    jReservation.setStatus(status);
    return reservationMapper.toDomain(jReservation);
  }
}
