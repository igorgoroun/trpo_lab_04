package trpo.lab_03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trpo.lab_03.db.LecturerDiscipline;

public interface LecturerDisciplineRepository extends JpaRepository<LecturerDiscipline, Integer> {
}