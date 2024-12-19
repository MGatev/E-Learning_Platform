package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
