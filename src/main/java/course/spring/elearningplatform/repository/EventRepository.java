package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);
}
