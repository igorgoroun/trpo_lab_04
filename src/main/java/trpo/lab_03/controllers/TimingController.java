package trpo.lab_03.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import trpo.lab_03.db.Room;
import trpo.lab_03.db.Timing;
import trpo.lab_03.repository.RoomRepository;
import trpo.lab_03.repository.TimingRepository;
import trpo.lab_03.services.RoomService;
import trpo.lab_03.services.TimingService;

import java.time.LocalTime;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class TimingController {
    private TimingService timingService;
    private final TimingRepository timingRepository;

    private static final String model_shortcut  = "timing";

    @GetMapping("/"+model_shortcut+"/list")
    public String timing_list(Model model) {
        model.addAttribute("dictionary", timingService.all());
        model.addAttribute("view_title", "Часи занять");
        model.addAttribute("model_shortcut", model_shortcut);
        return "timing/list";
    }

    @GetMapping("/"+model_shortcut+"/new")
    public String timing_new(Model model) {
        model.addAttribute("view_title", "Нова пара");
        model.addAttribute("model_shortcut", model_shortcut);
        model.addAttribute("form_action", "create");
        model.addAttribute("form_data", null);
        return "timing/form";
    }

    @PostMapping("/"+model_shortcut+"/create")
    public String timing_create(
            @RequestParam String name,
            @RequestParam LocalTime timeStart,
            @RequestParam LocalTime timeEnd
            ) {
        Timing r = new Timing(name, timeStart, timeEnd);
        timingService.create(r);
        return "redirect:/timing/" + r.getId();
    }

    @GetMapping("/"+model_shortcut+"/{timing_id}/edit")
    public String timing_edit(
            @PathVariable int timing_id,
            Model model
    ) {
        Optional<Timing> timing = timingService.getByPK(timing_id);
        timing.ifPresent(value -> model.addAttribute("form_data", value));
        model.addAttribute("view_title", "Змінити пару");
        model.addAttribute("form_action", "save");
        model.addAttribute("model_shortcut", model_shortcut);
        return "timing/form";
    }

    @PostMapping("/"+model_shortcut+"/save")
    public String timing_save(
            @RequestParam int timing_id,
            @RequestParam String name,
            @RequestParam LocalTime timeStart,
            @RequestParam LocalTime timeEnd
    ) {
        Optional<Timing> t = timingService.getByPK(timing_id);
        if (t.isPresent()) {
            Timing timing = t.get();
            timing.setName(name);
            timing.setTimeStart(timeStart);
            timing.setTimeEnd(timeEnd);
            timingRepository.save(timing);
            return "redirect:/timing/" + timing.getId();
        } else {
            return "redirect:/timing/list";
        }
    }

    @GetMapping("/"+model_shortcut+"/{id}/delete")
    public String timing_delete(@PathVariable int id) {
        timingService.delete(id);
        return "redirect:/timing/list";
    }

    @GetMapping("/"+model_shortcut+"/{timing_id}")
    public String timing_view(@PathVariable int timing_id, Model model) {
        Optional<Timing> timing = timingService.getByPK(timing_id);
        timing.ifPresent(value -> model.addAttribute("record", value));
        model.addAttribute("view_title", "Пара");
        model.addAttribute("model_shortcut", model_shortcut);
        return "timing/view";
    }

}
