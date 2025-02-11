package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.QuestionDto;
import course.spring.elearningplatform.entity.CustomUserDetails;
import course.spring.elearningplatform.service.ActivityLogService;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.impl.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("questions")
public class QuestionController {
    private final CourseService courseService;
    private final QuestionService questionService;
    private final ActivityLogService activityLogService;

    @Autowired
    public QuestionController(CourseService courseService, QuestionService questionService, ActivityLogService activityLogService) {
        this.courseService = courseService;
        this.questionService = questionService;
        this.activityLogService = activityLogService;
    }


    @PostMapping("create")
    public String createQuestion(@RequestParam long courseId, @ModelAttribute QuestionDto questionDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            courseService.addQuestionToCourse(courseId, questionDto);
            redirectAttributes.addFlashAttribute("successMessage", "Question created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create question: " + e.getMessage());
        }

        activityLogService.logActivity("Question created", customUserDetails.getUsername());
        return "redirect:/courses/" + courseId;
    }

    @GetMapping("question-create")
    public String showCreateQuestionPage(@RequestParam long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "question-create";
    }

    @PostMapping("question-delete")
    public String deleteQuestion(@RequestParam long courseId, @RequestParam long questionId, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        questionService.deleteQuestionFromCourse(courseId, questionId);
        questionService.deleteQuestionFromQuiz(questionId);
        redirectAttributes.addFlashAttribute("successMessage", "Question deleted successfully!");
        activityLogService.logActivity("Question deleted", customUserDetails.getUsername());
        return "redirect:/courses/" + courseId;
    }

    @GetMapping
    public String getAllQuestionsForCourse(@RequestParam long courseId, Model model) {
        model.addAttribute("questions", courseService.getAllQuestionsForCourse(courseId));
        model.addAttribute("courseId", courseId);
        return "questions";
    }
}
