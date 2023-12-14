package trpo.lab_03.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import trpo.lab_03.db.Lecturer;
import trpo.lab_03.db.Timing;
import trpo.lab_03.repository.LecturerRepository;
import trpo.lab_03.repository.TimingRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LecturerService {

    private final LecturerRepository repository;


    public List<Lecturer> all() {
        return repository.findAll();
    }

    public void create(Lecturer lecturer) {
        repository.save(lecturer);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Optional<Lecturer> getByPK(int id) {
        return repository.findById(id);
    }



}
