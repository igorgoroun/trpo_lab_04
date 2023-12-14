package trpo.lab_03.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import trpo.lab_03.db.Discipline;
import trpo.lab_03.db.Lecturer;
import trpo.lab_03.db.LecturerDiscipline;
import trpo.lab_03.repository.LecturerDisciplineRepository;
import trpo.lab_03.repository.LecturerRepository;
import trpo.lab_03.services.DisciplineService;
import trpo.lab_03.services.LecturerService;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class LecturerController {
    private LecturerService lecturerService;
    private final LecturerRepository lecturerRepository;
    private DisciplineService disciplineService;
    private LecturerDisciplineRepository lecturerDisciplineRepository;

    private static final String model_shortcut  = "lecturer";

    @GetMapping("/"+model_shortcut+"/list")
    public String lecturer_list(Model model) {
        model.addAttribute("dictionary", lecturerService.all());
        model.addAttribute("view_title", "Викладачі");
        model.addAttribute("model_shortcut", model_shortcut);
        return "lecturer/list";
    }

    @GetMapping("/"+model_shortcut+"/new")
    public String lecturer_new(Model model) {
        model.addAttribute("view_title", "Новий викладач");
        model.addAttribute("model_shortcut", model_shortcut);
        model.addAttribute("form_action", "create");
        model.addAttribute("form_data", null);
        return "form";
    }

    @PostMapping("/"+model_shortcut+"/create")
    public String lecturer_create(
            @RequestParam String name
    ) {
        Lecturer r = new Lecturer(name);
        lecturerService.create(r);
        return "redirect:/lecturer/" + r.getId();
    }

    @GetMapping("/"+model_shortcut+"/{lecturer_id}/edit")
    public String lecturer_edit(
            @PathVariable int lecturer_id,
            Model model
    ) {
        Optional<Lecturer> lecturer = lecturerService.getByPK(lecturer_id);
        lecturer.ifPresent(value -> model.addAttribute("form_data", value));
        model.addAttribute("view_title", "Змінити викладача");
        model.addAttribute("form_action", "save");
        model.addAttribute("model_shortcut", model_shortcut);
        return "form";
    }

    @PostMapping("/"+model_shortcut+"/save")
    public String lecturer_save(
            @RequestParam int lecturer_id,
            @RequestParam String name
    ) {
        Optional<Lecturer> p = lecturerService.getByPK(lecturer_id);
        if (p.isPresent()) {
            Lecturer lecturer = p.get();
            lecturer.setName(name);
            lecturerRepository.save(lecturer);
            return "redirect:/lecturer/" + lecturer.getId();
        } else {
            return "redirect:/lecturer/list";
        }
    }

    @GetMapping("/"+model_shortcut+"/{id}/delete")
    public String lecturer_delete(@PathVariable int id) {
        lecturerService.delete(id);
        return "redirect:/lecturer/list";
    }

    @GetMapping("/"+model_shortcut+"/{lecturer_id}")
    public String lecturer_view(@PathVariable int lecturer_id, Model model) {
        Optional<Lecturer> lecturer = lecturerService.getByPK(lecturer_id);
        if (lecturer.isPresent()) {
            model.addAttribute("available_disciplines", disciplineService.not_assigned_to(lecturer.get()));
            lecturer.ifPresent(value -> model.addAttribute("record", value));
            model.addAttribute("view_title", "Викладач");
            model.addAttribute("model_shortcut", model_shortcut);
            return "lecturer/view";
        } else {
            return "redirect:/lecturer/list";
        }

    }

    @PostMapping("/"+model_shortcut+"/{lecturer_id}/discipline/set")
    public String lecturer_discipline_set(
            @PathVariable int lecturer_id,
            @RequestParam int discipline_id
    ) {
        Optional<Lecturer> p = lecturerService.getByPK(lecturer_id);
        Optional<Discipline> d = disciplineService.getByPK(discipline_id);
        if (p.isPresent() && d.isPresent()) {
            LecturerDiscipline ld = new LecturerDiscipline();
            ld.setDiscipline(d.get());
            ld.setLecturer(p.get());
            lecturerDisciplineRepository.save(ld);
            return "redirect:/lecturer/" + p.get().getId();
        } else {
            return "redirect:/lecturer/list";
        }
    }

    @GetMapping("/"+model_shortcut+"/discipline/{ld_id}/unset")
    public String lecturer_discipline_unset(@PathVariable int ld_id) {
        Optional<LecturerDiscipline> ld = lecturerDisciplineRepository.findById(ld_id);
        if (ld.isPresent()) {
            Lecturer l = ld.get().getLecturer();
            lecturerDisciplineRepository.deleteById(ld_id);
            return "redirect:/lecturer/"+l.getId();
        } else {
            return "redirect:/lecturer/list";
        }
    }

}
