package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
    List<User> findAllByUsernameNotIn(List<String> usernames);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);

    Page<User> findAllByUsernameNotIn(List<String> usernames, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND u.username NOT IN (:excludedUsernames)")
    Page<User> searchUsersExcluding(@Param("query") String query, @Param("excludedUsernames") List<String> excludedUsernames, Pageable pageable);

    Page<User> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndUsernameNotIn(
            String firstNameQuery, String lastNameQuery, List<String> excludedUsernames, Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.completedLessons WHERE u.id = :userId")
    Optional<User> findByIdWithCompletedLessons(@Param("userId") Long userId);

    @Query("SELECT u.startedCourses FROM User u WHERE u.id = :userId")
    List<Course> findStartedCoursesByUserId(@Param("userId") Long userId);

    @Query("SELECT u.completedCourses FROM User u WHERE u.id = :userId")
    List<Course> findCompletedCoursesByUserId(@Param("userId") Long userId);
}
