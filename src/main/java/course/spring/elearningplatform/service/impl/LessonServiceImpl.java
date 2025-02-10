package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.LessonDto;
import course.spring.elearningplatform.dto.mapper.LessonDtoToLessonMapper;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Lesson;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.CourseRepository;
import course.spring.elearningplatform.repository.LessonRepository;
import course.spring.elearningplatform.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Lesson addLesson(LessonDto lessonDto, Course course) {
        Lesson lesson = LessonDtoToLessonMapper.mapLessonDtoToLesson(lessonDto, course);
        List<Lesson> lessons = course.getLessons();
        lessons.add(lesson);
        course.setLessons(lessons);
        Lesson savedLesson = lessonRepository.save(lesson);
        courseRepository.save(course);
        return savedLesson;
    }

    @Override
    public Lesson getLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
    }

    @Override
    public Lesson updateLessonDetails(Course course, Long id, String detail, Object value) {
        Lesson existingLesson = course.getLessonById(id);
        switch (detail) {
            case "lesson-title" -> existingLesson.setTitle((String) value);
            case "lesson-content" -> existingLesson.setContent((String) value);
            default -> throw new IllegalArgumentException("Invalid user detail: " + detail);
        }
        save(existingLesson);
        return existingLesson;
    }

    @Override
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
}