package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.mapper.QuestionDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.QuestionWrapper;
import course.spring.elearningplatform.entity.QuizDto;

import java.util.List;

public interface CourseService {
    Long getCourseQuizId(long courseId);

    Course getCourseById(long courseId);

    Course addQuestionToCourse(Long courseId, QuestionDto questionDto);

    Course addQuizToCourse(long courseId, QuizDto quizDto);

    List<QuestionWrapper> getQuestionsForCourseQuiz(Long courseId);
}
