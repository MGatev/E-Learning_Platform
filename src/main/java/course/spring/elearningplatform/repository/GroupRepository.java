package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByName(String name);
}
