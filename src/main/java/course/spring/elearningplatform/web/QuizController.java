package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.QuizSubmissionRequest;
import course.spring.elearningplatform.entity.*;
import course.spring.elearningplatform.service.ActivityLogService;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.UserService;
import course.spring.elearningplatform.service.impl.QuizzesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final ActivityLogService activityLogService;

    @Autowired
    public QuizController(QuizzesService quizzesService, CourseService courseService, UserService userService, ActivityLogService activityLogService) {
        this.courseService = courseService;
        this.quizzesService = quizzesService;
        this.userService = userService;
        this.activityLogService = activityLogService;
    }

    @PostMapping("create")
    public String createQuiz(@RequestParam long courseId, @ModelAttribute QuizDto quizDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            quizzesService.addQuizToCourse(courseId, quizDto);
            redirectAttributes.addFlashAttribute("successMessage", "Quiz created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create quiz: " + e.getMessage());
        }
        activityLogService.logActivity("Quiz created", userDetails.getUsername());
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
            model.addAttribute("loggedUser", loggedInUser);
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
    QuizSubmissionRequest submission, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Response> answers = submission.getAnswers();
        long elapsedTime = submission.getElapsedTime();
        activityLogService.logActivity("Quiz submitted", userDetails.getUsername());
        return quizzesService.calculateQuizResult(courseId, quizId, answers, elapsedTime);
    }

    @GetMapping("create-quiz")
    public String showCreateQuizPage(@RequestParam long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "create-quiz";
    }
}
