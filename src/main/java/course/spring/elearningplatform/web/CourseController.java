package course.spring.elearningplatform.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @GetMapping
    public String courses(Model model) {
        return "course";
    }
}
