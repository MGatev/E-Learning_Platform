package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Event;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final EventService eventService;
    private final CourseService courseService;

    @Autowired
    public HomeController(EventService eventService, CourseService courseService) {
        this.eventService = eventService;
        this.courseService = courseService;
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

        Map<String, List<Course>> coursesByCategory = courseService.getCoursesGroupedByCategory();
        model.addAttribute("coursesByCategory", coursesByCategory);

        return "home";
    }
}
