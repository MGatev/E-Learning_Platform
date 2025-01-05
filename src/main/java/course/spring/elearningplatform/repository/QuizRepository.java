package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q JOIN q.questions qu WHERE qu.id = :questionId")
    Quiz findByQuestionId(Long questionId);
}
