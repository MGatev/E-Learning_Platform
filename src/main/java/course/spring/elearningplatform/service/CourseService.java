package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.CourseDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Question;
import course.spring.elearningplatform.entity.Quiz;
import course.spring.elearningplatform.entity.StudentResult;
import course.spring.elearningplatform.entity.User;

import course.spring.elearningplatform.dto.QuestionDto;
import course.spring.elearningplatform.entity.QuestionWrapper;
import course.spring.elearningplatform.entity.QuizDto;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface CourseService {
    Course addCourse(CourseDto course, User user);

    Map<String, List<Course>> getCoursesGroupedByCategory();

    List<Course> getCoursesByCategory(String category);

    Course getCourseById(Long id);

    boolean areAllLessonsCompletedByUser(User user, Course course);

    Long getCourseQuizId(long courseId);

    Course getCourseById(long courseId);

    Course addQuestionToCourse(Long courseId, QuestionDto questionDto);

    @Transactional
    Course addQuizToCourse(long courseId, Quiz quiz);

    List<QuestionWrapper> getQuestionsForCourseQuiz(Long courseId);

    List<Question> getAllQuestionsForCourse(Long courseId);

    Course save(Course course);

    List<Course> getAllCourses();

    Course findById(Long courseId);

    Course startCourse(Long courseId, User user);

    Course completeCourse(Course course, User user);

    void addNewStudentResult(int percentage, long elapsedTime, long courseId);

    List<StudentResult> getHighScoresForCourse(Long courseId);
}

