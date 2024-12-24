package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.mapper.EntityMapper;
import course.spring.elearningplatform.dto.mapper.QuestionDto;
import course.spring.elearningplatform.entity.*;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.CourseRepository;
import course.spring.elearningplatform.repository.QuestionRepository;
import course.spring.elearningplatform.dto.CourseDto;
import course.spring.elearningplatform.dto.mapper.CourseDtoToCourseMapper;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final QuizzesService quizzesService;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final ImageService imageService;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             QuestionRepository questionRepository,
                             QuizzesService quizzesService,
                             ImageService imageService) {
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.quizzesService = quizzesService;
        this.imageService = imageService;
    }


    @Override
    public Course addCourse(CourseDto courseDto, User user) {
        if (courseRepository.existsByName(courseDto.getName())) {
            throw new DuplicatedEntityException(String.format(
                    "Cannot create a course with name '%s' because it already exists. Please choose other name.", courseDto.getName()));
        }

        Course course = CourseDtoToCourseMapper.mapCourseDtoToCourse(courseDto, user, imageService);
        return courseRepository.save(course);
    }

    @Override
    public Map<String, List<Course>> getCoursesGroupedByCategory() {
        List<Course> allCourses = courseRepository.findAll();
        allCourses = allCourses.stream().peek(course -> {
                    Image image = course.getImage();
                    if (image != null) {
                        course.setImageBase64(image.parseImage());
                    }
                })
                .toList();

        return allCourses.stream()
                .flatMap(course -> course.getCategories().stream()
                        .map(category -> new AbstractMap.SimpleEntry<>(category, course)))
                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())
                ));
    }

    @Override
    public Course getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no such course!"));
        Image image = course.getImage();
        if (image != null) {
            course.setImageBase64(image.parseImage());
        }
        return course;
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
            throw new EntityNotFoundException("There is no quiz available for that course");
        }
    }
}
