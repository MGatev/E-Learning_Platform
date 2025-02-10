package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.CourseAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends JpaRepository<CourseAnalytics, Long> {
}
