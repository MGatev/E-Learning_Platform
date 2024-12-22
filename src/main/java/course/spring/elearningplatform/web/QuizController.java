package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.QuestionWrapper;
import course.spring.elearningplatform.entity.QuizDto;
import course.spring.elearningplatform.entity.Response;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.impl.QuizzesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("quizzes")
public class QuizController {

    private final QuizzesService quizzesService;
    private final CourseService courseService;

    @Autowired
    public QuizController(QuizzesService quizzesService, CourseService courseService) {
        this.courseService = courseService;
        this.quizzesService = quizzesService;
    }

    @PostMapping("create")
    public ResponseEntity<Course> createQuiz(@RequestParam long courseId, @ModelAttribute QuizDto quizDto) {
        Course updatedCourse = courseService.addQuizToCourse(courseId, quizDto);
        return ResponseEntity.ok(updatedCourse);
    }


    @GetMapping("/quiz")
    public String showQuizPage(@RequestParam long courseId, Model model) {
        List<QuestionWrapper> quizQuestions = courseService.getQuestionsForCourseQuiz(courseId);
        var quizId = courseService.getCourseQuizId(courseId);

        model.addAttribute("quizId", quizId);
        model.addAttribute("courseId", courseId);
        model.addAttribute("quizQuestions", quizQuestions);

        return "quiz-submission";
    }

    @PostMapping("submit")
    public ResponseEntity<Map<String, Integer>> submitQuiz(@RequestParam("quizId") long quizId, @RequestBody() List<Response> answers) {
        return quizzesService.calculateQuizResult(quizId, answers);
    }

    @GetMapping("create-quiz")
    public String showCreateQuizPage(@RequestParam long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "create-quiz";
    }
}
