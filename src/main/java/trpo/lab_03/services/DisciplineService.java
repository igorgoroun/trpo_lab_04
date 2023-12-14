package trpo.lab_03.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import trpo.lab_03.db.Discipline;
import trpo.lab_03.db.Lecturer;
import trpo.lab_03.db.LecturerDiscipline;
import trpo.lab_03.repository.DisciplineRepository;
import trpo.lab_03.repository.LecturerDisciplineRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DisciplineService {

    private final DisciplineRepository repository;
    private LecturerDisciplineRepository lecturerDisciplineRepository;


    public List<Discipline> all() {
        return repository.findAll();
    }

    public List<Discipline> not_assigned_to(Lecturer lecturer) {
        List<Discipline> all_disciplines = repository.findAll();
        List<Discipline> assigned = new ArrayList<>();
        List<Discipline> not_assigned = new ArrayList<>();
        for (LecturerDiscipline ld: lecturer.getDisciplines()) {
            assigned.add(ld.getDiscipline());
        }
        for (Discipline d: all_disciplines) {
            if (!assigned.contains(d)) {
                not_assigned.add(d);
            }
        }
        return not_assigned;
    }

    public void create(Discipline discipline) {
        repository.save(discipline);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Optional<Discipline> getByPK(int id) {
        return repository.findById(id);
    }

}
