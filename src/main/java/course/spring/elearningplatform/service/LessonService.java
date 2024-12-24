package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.LessonDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Lesson;

public interface LessonService {
    Lesson addLesson(LessonDto lesson, Course course);

    Lesson getLessonById(Long lessonId);
}