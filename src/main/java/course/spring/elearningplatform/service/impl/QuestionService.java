package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Question;
import course.spring.elearningplatform.entity.Quiz;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.QuestionRepository;
import course.spring.elearningplatform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuizzesService quizzesService;

    private final CourseService courseService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, QuizzesService quizzesService, CourseService courseService) {
        this.questionRepository = questionRepository;
        this.quizzesService = quizzesService;
        this.courseService = courseService;
    }

    public ResponseEntity<List<Question>> getAllQuestions() {
        return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
    }

    public Question getQuestionById(long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Question with id %s not found", id)));
    }

    public void deleteQuestionFromQuiz(long id) {
        Quiz quizForQuestion = quizzesService.getQuizForQuestion(id);
        Question question = getQuestionById(id);
        if (quizForQuestion != null) {
            quizzesService.deleteQuestionFromQuiz(quizForQuestion.getId(), question);
        }
        questionRepository.delete(question);
    }

    public void deleteQuestionFromCourse(long courseId, long questionId) {
        Question question = getQuestionById(questionId);
        Course course = courseService.getCourseById(courseId);
        course.getQuestions().remove(question);
        courseService.save(course);
    }
}
