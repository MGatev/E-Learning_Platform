package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.CourseDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.User;

import course.spring.elearningplatform.dto.mapper.QuestionDto;
import course.spring.elearningplatform.entity.QuestionWrapper;
import course.spring.elearningplatform.entity.QuizDto;

import java.util.List;
import java.util.Map;

public interface CourseService {
    Course addCourse(CourseDto course, User user);

    Map<String, List<Course>> getCoursesGroupedByCategory();

    Course getCourseById(Long id);

    boolean areAllLessonsCompletedByUser(User user, Course course);
    Long getCourseQuizId(long courseId);

    Course getCourseById(long courseId);

    Course addQuestionToCourse(Long courseId, QuestionDto questionDto);

    Course addQuizToCourse(long courseId, QuizDto quizDto);

    List<QuestionWrapper> getQuestionsForCourseQuiz(Long courseId);
}
