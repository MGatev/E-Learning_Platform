package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.EventDto;
import course.spring.elearningplatform.dto.mapper.EntityMapper;
import course.spring.elearningplatform.entity.Event;
import course.spring.elearningplatform.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventServiceImpl eventService;

    @Autowired
    public EventController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

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

