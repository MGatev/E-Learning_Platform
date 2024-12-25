package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM question q WHERE q.course_id=:courseId ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestionsByCourseId(@Param("courseId") long courseId, @Param("limit") int numberOfQuestions);

    boolean existsByQuestionTitle(String questionTitle);
}
