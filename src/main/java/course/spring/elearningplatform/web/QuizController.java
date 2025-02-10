package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.QuizSubmissionRequest;
import course.spring.elearningplatform.entity.QuestionWrapper;
import course.spring.elearningplatform.entity.QuizDto;
import course.spring.elearningplatform.entity.Response;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.UserService;
import course.spring.elearningplatform.service.impl.QuizzesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("quizzes")
public class QuizController {

    private final QuizzesService quizzesService;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public QuizController(QuizzesService quizzesService, CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.quizzesService = quizzesService;
        this.userService = userService;
    }

    @PostMapping("create")
    public String createQuiz(@RequestParam long courseId, @ModelAttribute QuizDto quizDto, RedirectAttributes redirectAttributes) {
        try {
            quizzesService.addQuizToCourse(courseId, quizDto);
            redirectAttributes.addFlashAttribute("successMessage", "Quiz created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create quiz: " + e.getMessage());
        }

        return "redirect:/courses/" + courseId;
    }


    @GetMapping("/quiz")
    public String showQuizPage(@RequestParam long courseId, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<QuestionWrapper> quizQuestions = courseService.getQuestionsForCourseQuiz(courseId);
            var quizId = courseService.getCourseQuizId(courseId);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername();
            User loggedInUser = userService.getUserByUsername(username);

            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("quizId", quizId);
            model.addAttribute("courseId", courseId);
            model.addAttribute("quizQuestions", quizQuestions);

            return "quiz-submission";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to show quiz: " + e.getMessage());
        }

        return "redirect:/courses/" + courseId;
    }

    @PostMapping("submit")
    public ResponseEntity<Map<String, Integer>> submitQuiz(@RequestParam("courseId") long courseId, @RequestParam("quizId") long quizId, @RequestBody
    QuizSubmissionRequest submission) {
        List<Response> answers = submission.getAnswers();
        long elapsedTime = submission.getElapsedTime();
        return quizzesService.calculateQuizResult(courseId, quizId, answers, elapsedTime);
    }

    @GetMapping("create-quiz")
    public String showCreateQuizPage(@RequestParam long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "create-quiz";
    }
}
