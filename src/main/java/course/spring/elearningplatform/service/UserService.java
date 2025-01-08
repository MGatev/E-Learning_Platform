package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.UserDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Role;
import course.spring.elearningplatform.entity.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto user);
    User updateUser(UserDto user);
    User deleteUser(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    List<User> getAllUsersByRole(Role role);
    User updateUserDetails(Long id, String detail, Object value);
    User addStartedCourse(User user, Course course);
    User addCompletedCourse(User user, Course course);

    void save(User user);
}
