package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.entity.Certificate;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Question;
import course.spring.elearningplatform.entity.Quiz;
import course.spring.elearningplatform.entity.QuizDto;
import course.spring.elearningplatform.entity.Response;
import course.spring.elearningplatform.entity.StudentResult;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.CertificateRepository;
import course.spring.elearningplatform.repository.CourseRepository;
import course.spring.elearningplatform.repository.QuizRepository;
import course.spring.elearningplatform.repository.StudentResultRepository;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuizzesService {
    private final QuizRepository quizRepository;
    private final UserService userService;
    private final CourseService courseService;


    @Autowired
    public QuizzesService(QuizRepository quizRepository,
                          UserService userService,
                          CourseService courseService) {
        this.quizRepository = quizRepository;
        this.userService = userService;
        this.courseService = courseService;
    }

    public Quiz createQuiz(QuizDto quizDto, List<Question> quizQuestions) {
        if (quizQuestions.isEmpty()) {
            throw new EntityNotFoundException("There are no questions for creating a quiz. Try adding some.");
        }

        var quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());

        Collections.shuffle(quizQuestions);
        List<Question> selectedQuestions = quizQuestions.stream()
            .limit(quizDto.getNumberOfQuestions())
            .toList();

        quiz.setQuestions(new ArrayList<>(selectedQuestions));

        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(long id) {
        return quizRepository.findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("Quiz with id %s not found", id), "redirect:/home"));

    }

    public ResponseEntity<Map<String, Integer>> calculateQuizResult(long courseId, long quizId, List<Response> answers, long elapsedTime) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var quiz = quizOptional.get();
        List<Question> questionsDB = quiz.getQuestions();

        int rightAnswers =
            Math.toIntExact(answers.stream().filter(answer -> isCorrectAnswer(answer, questionsDB)).count());

        Map<String, Integer> result = new HashMap<>();
        result.put("score", rightAnswers);  // Correct answers
        result.put("totalQuestions", questionsDB.size());  // Total questions

        int percentage = Math.toIntExact(Math.round((rightAnswers * 100.0) / questionsDB.size()));
        result.put("percentage", percentage);

        courseService.addNewStudentResult(percentage, elapsedTime, courseId);

        return ResponseEntity.ok(result);
    }


    private boolean isCorrectAnswer(Response answer, List<Question> questions) {
        return questions.stream()
            .filter(question -> question.getId() == answer.getQuestionId())
            .findFirst()
            .map(question -> question.getCorrectAnswer().equals(answer.getAnswer()))
            .orElse(false);
    }


    private void completeCourse(Course course, User user, Certificate savedCertificate) {
        user.addCertificate(savedCertificate);

        course.removeParticipant(user);
        course.addStudentCompletedCourse(user);
        Course savedCourse = courseService.save(course);
        user.addCompletedCourse(savedCourse);

        userService.save(user);
    }

    public void deleteQuestionFromQuiz(long quizId, Question question) {
        var quiz = getQuizById(quizId);
        quiz.getQuestions().remove(question);
        quizRepository.save(quiz);
    }

    public Quiz getQuizForQuestion(long id) {
        return quizRepository.findByQuestionId(id);
    }

    public Course addQuizToCourse(long courseId, QuizDto quizDto) {
        var quiz = createQuiz(quizDto, courseService.getAllQuestionsForCourse(courseId));
        return courseService.addQuizToCourse(courseId, quiz);
    }
}
