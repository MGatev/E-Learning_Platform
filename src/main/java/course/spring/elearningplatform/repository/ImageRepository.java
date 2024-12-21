package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
