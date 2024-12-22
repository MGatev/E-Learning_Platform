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
import course.spring.elearningplatform.dto.CourseDto;
import course.spring.elearningplatform.dto.mapper.CourseDtoToCourseMapper;
import course.spring.elearningplatform.entity.Lesson;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Course addCourse(CourseDto courseDto, User user) {
        if (courseRepository.existsByName(courseDto.getName())) {
            throw new DuplicatedEntityException(String.format(
                    "Cannot create a course with name '%s' because it already exists. Please choose other name.", courseDto.getName()));
        }

        Course course = CourseDtoToCourseMapper.mapCourseDtoToCourse(courseDto, user);
        return courseRepository.save(course);
    }

    @Override
    public Map<String, List<Course>> getCoursesGroupedByCategory() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream()
                .flatMap(course -> course.getCategories().stream()
                        .map(category -> new AbstractMap.SimpleEntry<>(category, course)))
                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())
                ));
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("There is no such course!"));
    }

    @Override
    public boolean areAllLessonsCompletedByUser(User user, Course course) {
        Set<Lesson> completedLessons = user.getCompletedLessons();
        List<Lesson> courseLessons = course.getLessons();

        for (Lesson lesson : courseLessons) {
            if (!completedLessons.contains(lesson)) {
                return false;
            }
        }

        return true;
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
            //todo find better way to show that there is a problem
            throw new RuntimeException("Quiz not found for the course");
        }
    }
}
