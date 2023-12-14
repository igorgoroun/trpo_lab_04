package trpo.lab_03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trpo.lab_03.db.Lecturer;

public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
}