package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.CourseDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.User;

import java.util.List;
import java.util.Map;

public interface CourseService {
    Course addCourse(CourseDto course, User user);

    Map<String, List<Course>> getCoursesGroupedByCategory();

    Course getCourseById(Long id);

    boolean areAllLessonsCompletedByUser(User user, Course course);
}
