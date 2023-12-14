package trpo.lab_03.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import trpo.lab_03.db.Room;
import trpo.lab_03.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository repository;


    public List<Room> all() {
        return repository.findAll();
    }

    public void create(Room room) {
        repository.save(room);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Optional<Room> getByPK(int id) {
        return repository.findById(id);
    }

}
