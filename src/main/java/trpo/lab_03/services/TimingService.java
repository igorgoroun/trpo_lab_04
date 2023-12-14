package trpo.lab_03.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import trpo.lab_03.db.Timing;
import trpo.lab_03.repository.TimingRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TimingService {

    private final TimingRepository repository;


    public List<Timing> all() {
        return repository.findAll();
    }

    public void create(Timing timing) {
        repository.save(timing);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Optional<Timing> getByPK(int id) {
        return repository.findById(id);
    }

}
