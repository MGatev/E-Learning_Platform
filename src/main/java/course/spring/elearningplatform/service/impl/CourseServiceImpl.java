package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.mapper.EntityMapper;
import course.spring.elearningplatform.dto.mapper.QuestionDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Question;
import course.spring.elearningplatform.entity.QuestionWrapper;
import course.spring.elearningplatform.entity.Quiz;
import course.spring.elearningplatform.entity.QuizDto;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.CourseRepository;
import course.spring.elearningplatform.repository.QuestionRepository;
import course.spring.elearningplatform.service.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final QuizzesService quizzesService;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             QuestionRepository questionRepository,
                             QuizzesService quizzesService) {
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.quizzesService = quizzesService;
    }

    @Override
    public Long getCourseQuizId(long courseId) {
        var course = getCourseById(courseId);
        return course.getQuiz().getId();
    }

    @Override
    public Course getCourseById(long courseId) {
        //todo change redirect url
        return courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Course with id %s not found", courseId),
                "redirect:/groups"));
    }

    @Transactional
    @Override
    public Course addQuestionToCourse(Long courseId, QuestionDto questionDto) {
        Course course = getCourseById(courseId);
        var question = questionRepository.save(EntityMapper.mapCreateDtoToEntity(questionDto, Question.class));
        course.getQuestions().add(question);
        return courseRepository.save(course);
    }

    @Transactional
    @Override
    public Course addQuizToCourse(long courseId, QuizDto quizDto) {
        Course course = getCourseById(courseId);
        var quiz = quizzesService.createQuiz(quizDto, course.getQuestions());
        course.setQuiz(quiz);
        return courseRepository.saveAndFlush(course);
    }

    @Transactional
    @Override
    public List<QuestionWrapper> getQuestionsForCourseQuiz(Long courseId) {
        Course course = getCourseById(courseId);
        Quiz quiz = course.getQuiz();

        if (quiz != null) {
            List<Question> questionsDB = quiz.getQuestions();
            List<QuestionWrapper> questionsForUser = questionsDB.stream()
                .map(question -> new QuestionWrapper(question.getId(), question.getQuestionTitle(),
                    question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4()))
                .toList();

            return questionsForUser;
        } else {
            //todo find better way to show tgat there is a problem
            throw new RuntimeException("Quiz not found for the course");
        }
    }
}
