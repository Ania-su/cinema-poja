package school.hei.demo.service;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.demo.exception.NotFoundException;
import school.hei.demo.repository.RoomRepository;
import school.hei.demo.repository.model.JRoom;

@AllArgsConstructor
@Service
public class RoomService {

  private final RoomRepository repository;

  public JRoom findById(UUID roomId) {
    return repository.findById(roomId).orElseThrow(() -> new NotFoundException("Room not found"));
  }
}
