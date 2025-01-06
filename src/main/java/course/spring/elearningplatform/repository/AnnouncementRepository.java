package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
