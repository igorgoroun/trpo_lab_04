package trpo.lab_03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trpo.lab_03.db.Timing;

public interface TimingRepository extends JpaRepository<Timing, Integer> {
}