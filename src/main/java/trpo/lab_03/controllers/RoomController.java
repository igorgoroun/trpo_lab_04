package trpo.lab_03.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import trpo.lab_03.db.Room;
import trpo.lab_03.repository.RoomRepository;
import trpo.lab_03.services.RoomService;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class RoomController {
    private RoomService roomService;
    private final RoomRepository roomRepository;

    private static final String model_shortcut  = "room";

    @GetMapping("/"+model_shortcut+"/list")
    public String room_list(Model model) {
        model.addAttribute("dictionary", roomService.all());
        model.addAttribute("view_title", "Аудиторії");
        model.addAttribute("model_shortcut", model_shortcut);
        return "list";
    }

    @GetMapping("/"+model_shortcut+"/new")
    public String room_new(Model model) {
        model.addAttribute("view_title", "Нова аудиторія");
        model.addAttribute("model_shortcut", model_shortcut);
        model.addAttribute("form_action", "create");
        model.addAttribute("form_data", null);
        return "form";
    }

    @PostMapping("/"+model_shortcut+"/create")
    public String room_create(
            @RequestParam String name
    ) {
        Room r = new Room(name);
        roomService.create(r);
        return "redirect:/room/" + r.getId();
    }

    @GetMapping("/"+model_shortcut+"/{room_id}/edit")
    public String room_edit(
            @PathVariable int room_id,
            Model model
    ) {
        Optional<Room> room = roomService.getByPK(room_id);
        room.ifPresent(value -> model.addAttribute("form_data", value));
        model.addAttribute("view_title", "Змінити аудиторію");
        model.addAttribute("form_action", "save");
        model.addAttribute("model_shortcut", model_shortcut);
        return "form";
    }

    @PostMapping("/"+model_shortcut+"/save")
    public String room_save(
            @RequestParam int room_id,
            @RequestParam String name
    ) {
        Optional<Room> p = roomService.getByPK(room_id);
        if (p.isPresent()) {
            Room room = p.get();
            room.setName(name);
            roomRepository.save(room);
            return "redirect:/room/" + room.getId();
        } else {
            return "redirect:/room/list";
        }
    }

    @GetMapping("/"+model_shortcut+"/{id}/delete")
    public String room_delete(@PathVariable int id) {
        roomService.delete(id);
        return "redirect:/room/list";
    }

    @GetMapping("/"+model_shortcut+"/{room_id}")
    public String room_view(@PathVariable int room_id, Model model) {
        Optional<Room> room = roomService.getByPK(room_id);
        room.ifPresent(value -> model.addAttribute("record", value));
        model.addAttribute("view_title", "Aудиторія");
        model.addAttribute("model_shortcut", model_shortcut);
        return "view";
    }

}
