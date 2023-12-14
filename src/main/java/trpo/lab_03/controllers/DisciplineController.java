package trpo.lab_03.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import trpo.lab_03.db.Discipline;
import trpo.lab_03.repository.DisciplineRepository;
import trpo.lab_03.services.DisciplineService;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class DisciplineController {
    private DisciplineService disciplineService;
    private final DisciplineRepository disciplineRepository;

    private static final String model_shortcut  = "discipline";

    @GetMapping("/"+model_shortcut+"/list")
    public String discipline_list(Model model) {
        model.addAttribute("dictionary", disciplineService.all());
        model.addAttribute("view_title", "Предмети");
        model.addAttribute("model_shortcut", model_shortcut);
        return "list";
    }

    @GetMapping("/"+model_shortcut+"/new")
    public String discipline_new(Model model) {
        model.addAttribute("view_title", "Новий предмет");
        model.addAttribute("model_shortcut", model_shortcut);
        model.addAttribute("form_action", "create");
        model.addAttribute("form_data", null);
        return "form";
    }

    @PostMapping("/"+model_shortcut+"/create")
    public String discipline_create(
            @RequestParam String name
    ) {
        Discipline r = new Discipline(name);
        disciplineService.create(r);
        return "redirect:/discipline/" + r.getId();
    }

    @GetMapping("/"+model_shortcut+"/{discipline_id}/edit")
    public String discipline_edit(
            @PathVariable int discipline_id,
            Model model
    ) {
        Optional<Discipline> discipline = disciplineService.getByPK(discipline_id);
        discipline.ifPresent(value -> model.addAttribute("form_data", value));
        model.addAttribute("view_title", "Змінити предмет");
        model.addAttribute("form_action", "save");
        model.addAttribute("model_shortcut", model_shortcut);
        return "form";
    }

    @PostMapping("/"+model_shortcut+"/save")
    public String discipline_save(
            @RequestParam int discipline_id,
            @RequestParam String name
    ) {
        Optional<Discipline> p = disciplineService.getByPK(discipline_id);
        if (p.isPresent()) {
            Discipline discipline = p.get();
            discipline.setName(name);
            disciplineRepository.save(discipline);
            return "redirect:/discipline/" + discipline.getId();
        } else {
            return "redirect:/discipline/list";
        }
    }

    @GetMapping("/"+model_shortcut+"/{id}/delete")
    public String discipline_delete(@PathVariable int id) {
        disciplineService.delete(id);
        return "redirect:/discipline/list";
    }

    @GetMapping("/"+model_shortcut+"/{discipline_id}")
    public String discipline_view(@PathVariable int discipline_id, Model model) {
        Optional<Discipline> discipline = disciplineService.getByPK(discipline_id);
        discipline.ifPresent(value -> model.addAttribute("record", value));
        model.addAttribute("view_title", "Aудиторія");
        model.addAttribute("model_shortcut", model_shortcut);
        return "view";
    }

}
