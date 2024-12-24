package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.mapper.QuestionDto;
import course.spring.elearningplatform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("questions")
public class QuestionController {
    private final CourseService courseService;

    @Autowired
    public QuestionController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping("create")
    public String createQuestion(@RequestParam long courseId, @ModelAttribute QuestionDto questionDto) {
        courseService.addQuestionToCourse(courseId, questionDto);
        return "redirect:/courses/" + courseId;
    }

    @GetMapping("question-create")
    public String showCreateQuestionPage(@RequestParam long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "question-create";
    }
}
