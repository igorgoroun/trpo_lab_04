package trpo.lab_03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trpo.lab_03.db.Discipline;

public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {
}