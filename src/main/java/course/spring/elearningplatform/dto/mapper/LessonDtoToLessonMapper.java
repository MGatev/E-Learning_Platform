package course.spring.elearningplatform.dto.mapper;

import course.spring.elearningplatform.dto.LessonDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Lesson;

import java.util.Date;

public class LessonDtoToLessonMapper {
    public static Lesson mapLessonDtoToLesson(LessonDto lessonDto, Course course) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setContent(lessonDto.getContent());
        lesson.setCreatedOn(new Date());
        lesson.setRelatedCourse(course);

        return lesson;
    }
}