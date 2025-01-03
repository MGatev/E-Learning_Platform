package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.AssignmentDto;
import course.spring.elearningplatform.dto.mapper.EntityMapper;
import course.spring.elearningplatform.entity.Assignment;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Event;
import course.spring.elearningplatform.service.AssignmentService;
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
    private final AssignmentService assignmentService;

    @Autowired
    public HomeController(EventService eventService, CourseService courseService, AssignmentService assignmentService) {
        this.eventService = eventService;
        this.courseService = courseService;
        this.assignmentService = assignmentService;
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
        Map<String, List<Course>> top3CoursesByCategory = coursesByCategory.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // Keep the category key as it is
                        entry -> entry.getValue().stream() // Process the list of courses
                                .limit(3) // Limit to the first 3 courses
                                .toList() // Collect into a new list
                ));

        model.addAttribute("top3CoursesByCategory", top3CoursesByCategory);

        List<AssignmentDto> assignmentDtos = assignmentService.getAllAssignments();

        List<Assignment> assignments = assignmentDtos.stream()
                .map(dto -> EntityMapper.mapCreateDtoToEntity(dto, Assignment.class))
                .toList();

        List<Assignment> upcomingAssignments = assignments.stream()
                .filter(a -> a.getDueDate().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Assignment::getDueDate))
                .limit(3)
                .collect(Collectors.toList());

        model.addAttribute("assignments", upcomingAssignments);
        return "home";
    }
}
