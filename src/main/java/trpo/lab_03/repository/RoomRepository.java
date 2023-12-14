package trpo.lab_03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trpo.lab_03.db.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}