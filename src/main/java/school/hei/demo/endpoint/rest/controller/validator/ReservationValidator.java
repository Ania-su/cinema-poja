package school.hei.demo.endpoint.rest.controller.validator;

import org.springframework.stereotype.Component;
import school.hei.demo.endpoint.rest.controller.dto.ReservationRequest;
import school.hei.demo.entity.enums.ReservationStatus;
import school.hei.demo.exception.BadRequestException;

@Component
public class ReservationValidator {
  public void validate(ReservationStatus status) {
    if (status == null) {
      throw new BadRequestException("Reservation status cannot be empty. ");
    }
  }

  public void validate(ReservationRequest reservationRequest) {
    StringBuilder errors = new StringBuilder();

    if (reservationRequest.getStatus() == null) {
      errors.append("Reservation status cannot be empty. ");
    }
    if (reservationRequest.getProjectionId() == null) {
      errors.append("Projection id cannot be empty. ");
    }
    if (reservationRequest.getClientId() == null) {
      errors.append("Client id cannot be empty. ");
    }
    if (reservationRequest.getSeatIds() == null || reservationRequest.getSeatIds().isEmpty()) {
      errors.append("At least one seat is required. ");
    }

    if (!errors.isEmpty()) {
      throw new BadRequestException(errors.toString().trim());
    }
  }
}
