package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.CustomUserDetails;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor")
@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
public class InstructorController {

    private final CourseService courseService;
    private final EventService eventService;

    @Autowired
    public InstructorController(CourseService courseService, EventService eventService) {
        this.courseService = courseService;
        this.eventService = eventService;
    }

    @GetMapping("/courses")
    public String showCourses(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, HttpServletRequest request) {
        model.addAttribute("loggedUser", userDetails.getUser());
        model.addAttribute("courses", courseService.getAllCoursesByUser(userDetails.getUser()));
        return "instructor-courses";
    }

    @GetMapping("/events")
    public String showEvents(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, HttpServletRequest request) {
        model.addAttribute("loggedUser", userDetails.getUser());
        model.addAttribute("events", eventService.getAllEventsByUser(userDetails.getUsername()));
        return "instructor-events";
    }
}
