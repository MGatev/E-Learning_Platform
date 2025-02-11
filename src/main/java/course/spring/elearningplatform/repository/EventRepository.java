package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Event;
import course.spring.elearningplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);
    List<Event> findByInstructor(String instructor);
}
