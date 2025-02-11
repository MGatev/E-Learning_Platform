package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.UserDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Role;
import course.spring.elearningplatform.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User createUser(UserDto user);
    User updateUser(User user);
    void deleteUser(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    List<User> getAllUsersExcept(List<String> users);
    List<User> getAllUsersByRole(Role role);
    User updateUserDetails(Long id, String detail, Object value);
    User addStartedCourse(User user, Course course);
    User addCompletedCourse(User user, Course course);
    User findByIdWithCompletedLessons(Long id);

    void save(User user);
    Page<User> getAllUsers(int page, int size, String loggedInUsername);
    Page<User> searchUsers(String searchQuery, int page, int size, String loggedInUsername);
}
