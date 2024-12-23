package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
