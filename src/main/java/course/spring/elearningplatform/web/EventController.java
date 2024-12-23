package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.EventDto;
import course.spring.elearningplatform.dto.ImageDto;
import course.spring.elearningplatform.dto.mapper.EntityMapper;
import course.spring.elearningplatform.entity.Event;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.service.EventService;
import course.spring.elearningplatform.service.ImageService;
import course.spring.elearningplatform.service.impl.EventServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventServiceImpl eventService;

    private final ImageService imageService;

    @GetMapping
    public String getAllEvents(Model model) {
        List<EventDto> events = eventService.getAllEvents().stream()
                .map(event -> EntityMapper.mapEntityToDto(event, EventDto.class))
                .collect(Collectors.toList());
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/{id}")
    public String getEventById(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        event.setImageBase64(event.getImage().parseImage());
        EventDto eventDTO = EntityMapper.mapEntityToDto(event, EventDto.class);
        model.addAttribute("event", event);
        return "event-detail";
    }

    @GetMapping("/new")
    public String createEventForm(Model model) {
        EventDto eventDto = new EventDto();
        model.addAttribute("event", eventDto);
        return "event-form";
    }

    @PostMapping
    public String saveEvent(@ModelAttribute EventDto eventDTO) {
        eventService.saveEvent(eventDTO);
        return "redirect:/events";
    }


    @GetMapping("/home")
    public String homePage(Model model) {
        List<Event> allEvents = eventService.getAllEvents();

        List<Event> upcomingEvents = allEvents.stream()
                .filter(event -> event.getStartTime().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Event::getStartTime))
                .limit(3)
                .collect(Collectors.toList());

        model.addAttribute("events", allEvents);
        model.addAttribute("upcomingEvents", upcomingEvents);
        return "home";
    }


    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }
}

