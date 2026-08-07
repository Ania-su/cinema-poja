package school.hei.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.demo.endpoint.rest.controller.dto.ReservationRequest;
import school.hei.demo.entity.Reservation;
import school.hei.demo.entity.enums.ReservationStatus;
import school.hei.demo.exception.NotFoundException;
import school.hei.demo.repository.ProjectionRepository;
import school.hei.demo.repository.ReservationRepository;
import school.hei.demo.repository.SeatRepository;
import school.hei.demo.repository.UserRepository;
import school.hei.demo.repository.mapper.ReservationMapper;
import school.hei.demo.repository.model.JProjection;
import school.hei.demo.repository.model.JReservation;
import school.hei.demo.repository.model.JSeat;
import school.hei.demo.repository.model.JUser;

@AllArgsConstructor
@Service
public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final ProjectionRepository projectionRepository;
  private final UserRepository userRepository;
  private final SeatRepository seatRepository;
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
    JReservation saved = reservationRepository.save(jReservation);
    return reservationMapper.toDomain(saved);
  }

  public Reservation createReservation(ReservationRequest newReservation) {
    JProjection jProjection =
        projectionRepository
            .findById(newReservation.getProjectionId())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "Projection not found with id: " + newReservation.getProjectionId()));

    JUser jClient =
        userRepository
            .findById(newReservation.getClientId())
            .orElseThrow(() -> new NotFoundException("Client not found"));

    List<JSeat> jSeats = seatRepository.findAllById(newReservation.getSeatIds());
    if (jSeats.size() != newReservation.getSeatIds().size()) {
      throw new NotFoundException("One or more seats not found");
    }

    JReservation jReservation = new JReservation();
    jReservation.setCreatedAt(newReservation.getCreatedAt());
    jReservation.setStatus(newReservation.getStatus());
    jReservation.setProjection(jProjection);
    jReservation.setClient(jClient);
    jReservation.setSeats(jSeats);

    JReservation saved = reservationRepository.save(jReservation);
    return reservationMapper.toDomain(saved);
  }
}
